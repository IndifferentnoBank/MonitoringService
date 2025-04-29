package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.DeletedTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedTokensRepository extends JpaRepository<DeletedTokens, String> {
}
