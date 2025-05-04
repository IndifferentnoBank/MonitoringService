package bank.indeferentno.MonitoringService.model.output;

import bank.indeferentno.MonitoringService.model.enumeration.EventTypeRole;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record LogProjection(
    String serviceName,
    EventTypeRole eventType,
    String logMessage,
    OffsetDateTime timestamp
) {}
