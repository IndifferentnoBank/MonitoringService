package bank.indeferentno.MonitoringService.service;

import bank.indeferentno.MonitoringService.config.JwtTokenProvider;
import bank.indeferentno.MonitoringService.config.KafkaProducer;
import bank.indeferentno.MonitoringService.entity.DeletedTokens;
import bank.indeferentno.MonitoringService.entity.User;
import bank.indeferentno.MonitoringService.entity.UserRole;
import bank.indeferentno.MonitoringService.exception.NotFoundException;
import bank.indeferentno.MonitoringService.exception.UnauthorizedException;
import bank.indeferentno.MonitoringService.model.*;
import bank.indeferentno.MonitoringService.repository.DeletedTokensRepository;
import bank.indeferentno.MonitoringService.repository.UserRepository;
import bank.indeferentno.MonitoringService.repository.UserRoleRepository;
import bank.indeferentno.MonitoringService.model.CreateUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final DeletedTokensRepository deletedTokensRepository;
    private final KafkaProducer kafkaProducer;
    private final UserRoleRepository userRoleRepository;
    private final WebClient webClient;

    @Value("${user-service.port}")
    private String portUrl;
    @Value("${user-service.host}")
    private String hostUrl;

    @SneakyThrows
    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Incorrect login or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new NotFoundException("Incorrect login or password");
        }

        return jwtTokenProvider.generateToken(user);
    }

    public String register(CreateUserDto request) {
        RegisterDto registerDto = new RegisterDto(request.email(), request.phoneNumber(), request.fullName(), request.passport(), request.roles());

        try {
            UserIdDto userIdDto =  webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")  // Указываем схему (http/https)
                            .host(hostUrl)  // Указываем хост
                            .port(portUrl)  // Указываем порт
                            .path("/api/users")
                            .build())
                    .bodyValue(registerDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).flatMap(body -> {
                                log.error("Error with request к AuthService: status={}, body={}", response.statusCode(), body);
                                return Mono.error(new WebClientResponseException(
                                        response.statusCode().value(),
                                        "Error with call AuthService",
                                        response.headers().asHttpHeaders(),
                                        body.getBytes(),
                                        StandardCharsets.UTF_8));
                            })
                    )
                    .bodyToMono(UserIdDto.class)
                    .block();

            //UserIdDto userIdDto = new UserIdDto(UUID.randomUUID());
            String encodedPassword = passwordEncoder.encode(request.password());
            assert userIdDto != null;

            User newUser = new User(userIdDto.id(), request.email(), encodedPassword);

            for (RoleEnum roleEnum : request.roles()) {
                UserRole role = new UserRole();
                role.setRole(roleEnum);
                role.setUserId(userIdDto.id());
                userRoleRepository.save(role); // установит связь user -> role
            }

            userRepository.save(newUser);

            return jwtTokenProvider.generateToken(newUser);
        } catch (WebClientResponseException ex) {
            log.error("Error WebClient: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex; // Прокидываем дальше
        }
    }

    @SneakyThrows
    public Void editUserById(Authentication authentication, String token,CreateUserDto request, UUID id) {
        UUID userId = jwtTokenProvider.getUserIdFromAuthentication(authentication);

        if (deletedTokensRepository.findById(token).isPresent()) {
            throw new UnauthorizedException("The user is not authorized");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with not fount"));

        RegisterDto registerDto = new RegisterDto(request.email(), request.phoneNumber(), request.fullName(), request.passport(), request.roles());

        try {
            UserIdDto userIdDto =  webClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")  // Указываем схему (http/https)
                            .host(hostUrl)  // Указываем хост
                            .port(portUrl)  // Указываем порт
                            .path("/api/users/{id}")
                            .build(id))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(registerDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).flatMap(body -> {
                                log.error("Error with request к AuthService: status={}, body={}", response.statusCode(), body);
                                return Mono.error(new WebClientResponseException(
                                        response.statusCode().value(),
                                        "Error with call AuthService",
                                        response.headers().asHttpHeaders(),
                                        body.getBytes(),
                                        StandardCharsets.UTF_8));
                            })
                    )
                    .bodyToMono(UserIdDto.class)
                    .block();

            //UserIdDto userIdDto = new UserIdDto(UUID.randomUUID());
            String encodedPassword = passwordEncoder.encode(request.password());
            assert userIdDto != null;

            user.setEmail(request.email());
            user.setPassword(encodedPassword);

            userRepository.save(user);

            return null;
        } catch (WebClientResponseException ex) {
            log.error("Error WebClient: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex; // Прокидываем дальше
        }
    }

    @SneakyThrows
    public Boolean logout(Authentication authentication, String token) {
        UUID userId = jwtTokenProvider.getUserIdFromAuthentication(authentication);

        if (deletedTokensRepository.findById(token).isPresent()) {
            throw new UnauthorizedException("The user is not authorized");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonmap = new HashMap<>();
        jsonmap.put("deleted_token", token);

        try {
            String deleted_token = mapper.writeValueAsString(jsonmap);
            kafkaProducer.sendMessage("BANK.deleted_tokens", deleted_token);

            DeletedTokens deletedToken = DeletedTokens.of(token);
            deletedTokensRepository.save(deletedToken);
        } catch (Exception e) {
            log.error("Не получилось серелизовать токен: {}", token);
            throw new RuntimeException("Ошибка Серелизации в JSON", e);
        }

        return true;
    }
}
