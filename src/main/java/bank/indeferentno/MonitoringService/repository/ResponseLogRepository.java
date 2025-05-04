package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.RequestLog;
import bank.indeferentno.MonitoringService.entity.ResponseLog;
import bank.indeferentno.MonitoringService.model.output.LogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ResponseLogRepository extends JpaRepository<ResponseLog, UUID> {
    @Query("SELECT r FROM ResponseLog r")
    List<ResponseLog> findLogs();

    List<ResponseLog> findByTraceId(String traceId);
}
