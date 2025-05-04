package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.ErrorLog;
import bank.indeferentno.MonitoringService.entity.RequestLog;
import bank.indeferentno.MonitoringService.model.output.LogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, UUID> {
    @Query("SELECT e FROM ErrorLog e")
    List<ErrorLog> findErrors();

    List<ErrorLog> findByTraceId(String traceId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM ErrorLog e WHERE e.traceId = :traceId")
    boolean existsByTraceIdCustom(@Param("traceId") String traceId);
}
