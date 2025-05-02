package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.ErrorLog;
import bank.indeferentno.MonitoringService.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, UUID> {
}
