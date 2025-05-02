package bank.indeferentno.MonitoringService.entity;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "response_logs")
public class ResponseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "event_type")
    private EventTypeRole eventType;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "span_id")
    private String spanId;

    private LocalDateTime timestamp;

    @Column(name = "log_message")
    private String logMessage;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "http_status")
    private String httpStatus;
}
