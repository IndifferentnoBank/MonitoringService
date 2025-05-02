package bank.indeferentno.MonitoringService.config;

import bank.indeferentno.MonitoringService.service.MonitoringService;
import bank.indeferentno.MonitoringService.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MonitoringService monitoringService;
    private final TokenService tokenService;

    @KafkaListener(topics = "${monitoring-service.consumer.topic.response}")
    public void listenResponse(String message) {
        log.info("Message received: {}", message);
        monitoringService.parsingMessageResponse(message);
    }

    @KafkaListener(topics = "${monitoring-service.consumer.topic.request}")
    public void listenRequest(String message) {
        log.info("Message received: {}", message);
        monitoringService.parsingMessageRequest(message);
    }

    @KafkaListener(topics = "${monitoring-service.consumer.topic.error}")
    public void listenError(String message) {
        log.info("Message received: {}", message);
        monitoringService.parsingMessageError(message);
    }

    @KafkaListener(topics = "${monitoring-service.consumer.topic.deleted-tokens}")
    public void listenTokens(String message) {
        log.info("Message received: {}", message);
        tokenService.addDeletedToken(message);
    }
}
