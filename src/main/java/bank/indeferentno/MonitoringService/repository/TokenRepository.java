package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
