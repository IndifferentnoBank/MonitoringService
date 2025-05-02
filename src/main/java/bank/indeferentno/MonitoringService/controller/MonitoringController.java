package bank.indeferentno.MonitoringService.controller;

import bank.indeferentno.MonitoringService.exception.UnauthorizedException;
import bank.indeferentno.MonitoringService.model.output.Log;
import bank.indeferentno.MonitoringService.model.output.LogProjection;
import bank.indeferentno.MonitoringService.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @GetMapping("/log")
    @SneakyThrows
    public ResponseEntity<List<LogProjection>> getAllLogs(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(monitoringService.getAllLogs(auth, token));
        }
        throw new UnauthorizedException("Invalid Authorization header");
    }
}
