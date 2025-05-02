package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.ErrorLog;
import bank.indeferentno.MonitoringService.entity.RequestLog;
import bank.indeferentno.MonitoringService.model.output.LogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, UUID> {
    @Query("SELECT r.serviceName as serviceName, r.eventType as eventType, " +
            "r.logMessage as logMessage, r.timestamp as timestamp " +
            "FROM ErrorLog r")
    List<LogProjection> findLogs();
}
