package bank.indeferentno.MonitoringService.controller;

import bank.indeferentno.MonitoringService.exception.ForbiddenException;
import bank.indeferentno.MonitoringService.exception.NotFoundException;
import bank.indeferentno.MonitoringService.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> resolveResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getStatusCode().value(), ex.getReason(), request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred", request);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleWebClientResponseException(WebClientResponseException ex, HttpServletRequest request) {
        try {
            // Парсим JSON-ответ от AuthService
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> originalResponse = objectMapper.readValue(ex.getResponseBodyAsString(), Map.class);

            // Логируем оригинальный ответ
            log.error("Ошибка от внешнего сервиса: {}", originalResponse);

            return ResponseEntity.status(ex.getStatusCode()).body(originalResponse);
        } catch (Exception parseEx) {
            log.error("Не удалось обработать JSON-ответ от AuthService", parseEx);
            return buildErrorResponse(ex.getStatusCode().value(), ex.getMessage(), request);
        }
    }


    private ResponseEntity<Object> buildErrorResponse(int status, String message, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("message", message);
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.valueOf(status));
    }
}

