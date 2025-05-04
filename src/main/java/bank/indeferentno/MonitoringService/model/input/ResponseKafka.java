package bank.indeferentno.MonitoringService.model.input;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ResponseKafka(
        String service_name,
        EventTypeRole event_type,
        String trace_id,
        String span_id,
        Integer duration_ms,
        String timestamp,
        String log_message,
        DataRequest data
) {
}

