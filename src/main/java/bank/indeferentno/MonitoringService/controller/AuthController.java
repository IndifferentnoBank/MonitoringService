package bank.indeferentno.MonitoringService.controller;

import bank.indeferentno.MonitoringService.exception.UnauthorizedException;
import bank.indeferentno.MonitoringService.model.CreateUserDto;
import bank.indeferentno.MonitoringService.model.LoginRequest;
import bank.indeferentno.MonitoringService.model.LoginResponse;
import bank.indeferentno.MonitoringService.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody CreateUserDto request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @SneakyThrows
    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> editUserById(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader, @RequestBody CreateUserDto request, @PathVariable("id") UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(authService.editUserById(auth, token, request,id));
        }
        throw new UnauthorizedException("Invalid Authorization header");
    }

    @GetMapping("/logout")
    @SneakyThrows
    public ResponseEntity<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(authService.logout(auth, token));
        }
        throw new UnauthorizedException("Invalid Authorization header");
    }
}

