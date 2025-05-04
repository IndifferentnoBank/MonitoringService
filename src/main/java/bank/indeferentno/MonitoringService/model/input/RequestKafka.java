package bank.indeferentno.MonitoringService.model.input;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record RequestKafka(
        String service_name,
        EventTypeRole event_type,
        String trace_id,
        String parent_span_id,
        String span_id,
        //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        String timestamp,
        String log_message,
        DataKafka data
) {
}
