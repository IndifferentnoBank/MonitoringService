package bank.indeferentno.MonitoringService.model.input;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;

import java.time.LocalDateTime;

public record ErrorKafka(
        String service_name,
        EventTypeRole event_type,
        String trace_id,
        String span_id,
        LocalDateTime timestamp,
        String log_message
) {
}
