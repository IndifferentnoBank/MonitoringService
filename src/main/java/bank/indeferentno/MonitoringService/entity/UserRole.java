package bank.indeferentno.MonitoringService.entity;

import bank.indeferentno.MonitoringService.model.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "user_id")
    private UUID userId;
}