package bank.indeferentno.MonitoringService.entity;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "error_logs")
public class ErrorLog {
    @Id
    private UUID id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "event_type")
    private EventTypeRole eventType;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "span_id")
    private String spanId;

    private OffsetDateTime timestamp;

    @Column(name = "log_message")
    private String logMessage;
}
