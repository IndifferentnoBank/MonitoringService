package bank.indeferentno.MonitoringService.service;

import bank.indeferentno.MonitoringService.entity.ResponseLog;
import bank.indeferentno.MonitoringService.entity.Token;
import bank.indeferentno.MonitoringService.model.input.DeletedTokenKafka;
import bank.indeferentno.MonitoringService.model.input.ResponseKafka;
import bank.indeferentno.MonitoringService.repository.TokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    @SneakyThrows
    public void addDeletedToken(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DeletedTokenKafka deletedTokenKafka = objectMapper.readValue(message, DeletedTokenKafka.class);
            Token token = new Token(deletedTokenKafka.deleted_token());
            tokenRepository.save(token);
        } catch (JsonProcessingException e) {
            log.error("Error parsing the message: {}", message);
            throw new RuntimeException(e);
        }
    }
}
