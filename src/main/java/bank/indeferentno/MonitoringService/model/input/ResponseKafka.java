package bank.indeferentno.MonitoringService.model.input;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;

import java.time.LocalDateTime;

public record ResponseKafka(
        String service_name,
        EventTypeRole event_type,
        String trace_id,
        String span_id,
        Integer duration_ms,
        LocalDateTime timestamp,
        String log_message,
        DataRequest data
) {
}

