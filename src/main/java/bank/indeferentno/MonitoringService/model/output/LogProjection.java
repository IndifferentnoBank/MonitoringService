package bank.indeferentno.MonitoringService.model.output;

import java.time.LocalDateTime;

public interface LogProjection {
    String getServiceName();
    String getEventType();
    String getLogMessage();
    LocalDateTime getTimestamp();
}
