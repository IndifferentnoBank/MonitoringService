package bank.indeferentno.MonitoringService.service;

import bank.indeferentno.MonitoringService.config.JwtTokenProvider;
import bank.indeferentno.MonitoringService.entity.ErrorLog;
import bank.indeferentno.MonitoringService.entity.RequestLog;
import bank.indeferentno.MonitoringService.entity.ResponseLog;
import bank.indeferentno.MonitoringService.exception.UnauthorizedException;
import bank.indeferentno.MonitoringService.model.input.ErrorKafka;
import bank.indeferentno.MonitoringService.model.input.RequestKafka;
import bank.indeferentno.MonitoringService.model.input.ResponseKafka;
import bank.indeferentno.MonitoringService.model.output.Log;
import bank.indeferentno.MonitoringService.model.output.LogProjection;
import bank.indeferentno.MonitoringService.repository.ErrorLogRepository;
import bank.indeferentno.MonitoringService.repository.RequestLogRepository;
import bank.indeferentno.MonitoringService.repository.ResponseLogRepository;
import bank.indeferentno.MonitoringService.repository.TokenRepository;
import ch.qos.logback.core.spi.ErrorCodes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonitoringService {

    private final ErrorLogRepository errorLogRepository;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    @SneakyThrows
    public void parsingMessageRequest(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RequestKafka requestKafka = objectMapper.readValue(message, RequestKafka.class);
            RequestLog requestLog = new RequestLog(UUID.randomUUID(), requestKafka.service_name(), requestKafka.event_type(),
                    requestKafka.trace_id(), requestKafka.parent_span_id(), requestKafka.span_id(), requestKafka.timestamp(),
                    requestKafka.log_message(), requestKafka.data().http_method(), requestKafka.data().url());

            requestLogRepository.save(requestLog);
        } catch (JsonProcessingException e) {
            log.error("Error parsing the message: {}", message);
            throw new RuntimeException(e);
        }
    }
    @SneakyThrows
    public void parsingMessageResponse(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseKafka responseKafka = objectMapper.readValue(message, ResponseKafka.class);
            ResponseLog responseLog = new ResponseLog(UUID.randomUUID(), responseKafka.service_name(), responseKafka.event_type(),
                    responseKafka.trace_id(), responseKafka.span_id(), responseKafka.timestamp(),
                    responseKafka.log_message(), responseKafka.duration_ms(), responseKafka.data().http_status());

            responseLogRepository.save(responseLog);
        } catch (JsonProcessingException e) {
            log.error("Error parsing the message: {}", message);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void parsingMessageError(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorKafka errorKafka = objectMapper.readValue(message, ErrorKafka.class);
            ErrorLog errorLog = new ErrorLog(UUID.randomUUID(), errorKafka.service_name(), errorKafka.event_type(),
                    errorKafka.trace_id(), errorKafka.span_id(), errorKafka.timestamp(),
                    errorKafka.log_message());

            errorLogRepository.save(errorLog);
        } catch (JsonProcessingException e) {
            log.error("Error parsing the message: {}", message);
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public List<LogProjection> getAllLogs(Authentication auth, String token) {
        UUID userId = jwtTokenProvider.getUserIdFromAuthentication(auth);

        if (tokenRepository.findById(token).isPresent()) {
            throw new UnauthorizedException("The user is not authorized");
        }

        List<LogProjection> logs = new ArrayList<>();
        logs.addAll(requestLogRepository.findLogs());
        logs.addAll(responseLogRepository.findLogs());

        return logs;
    }
    @SneakyThrows
    public List<LogProjection> getAllErrors(Authentication auth, String token) {
        UUID userId = jwtTokenProvider.getUserIdFromAuthentication(auth);

        if (tokenRepository.findById(token).isPresent()) {
            throw new UnauthorizedException("The user is not authorized");
        }

        return new ArrayList<>(errorLogRepository.findLogs());
    }
}
