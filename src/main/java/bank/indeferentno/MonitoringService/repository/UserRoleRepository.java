package bank.indeferentno.MonitoringService.repository;

import bank.indeferentno.MonitoringService.entity.UserRole;
import bank.indeferentno.MonitoringService.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    @Query("SELECT r.role FROM UserRole r WHERE userId = :id")
    List<RoleEnum> getRolesByUser(@Param("id") UUID id);
}
