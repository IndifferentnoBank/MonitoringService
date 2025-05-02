package bank.indeferentno.MonitoringService.entity;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request_logs")
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "event_type")
    private EventTypeRole eventType;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "parent_span_id")
    private String parentSpanId;

    @Column(name = "span_id")
    private String spanId;

    private LocalDateTime timestamp;

    @Column(name = "log_message")
    private String logMessage;

    @Column(name = "http_method")
    private String httpMethod;

    private String url;
}
