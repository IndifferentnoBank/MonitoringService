package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.RequestLog;
import bank.indeferentno.MonitoringService.model.output.LogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {

    @Query("SELECT r FROM RequestLog r")
    List<RequestLog> findLogs();

    List<RequestLog> findByTraceId(String traceId);

    @Query("SELECT DISTINCT r.traceId FROM RequestLog r")
    List<String> findAllTraceIds();

    @Query("SELECT MIN(r.timestamp) FROM RequestLog r WHERE r.traceId = :traceId")
    Optional<OffsetDateTime> findFirstEventTime(@Param("traceId") String traceId);

    @Query("SELECT MAX(r.timestamp) FROM RequestLog r WHERE r.traceId = :traceId")
    Optional<OffsetDateTime> findLastEventTime(@Param("traceId") String traceId);

    @Query("SELECT COUNT(DISTINCT r.spanId) FROM RequestLog r WHERE r.traceId = :traceId")
    Long countSpansByTraceId(@Param("traceId") String traceId);
}
