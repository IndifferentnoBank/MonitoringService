package bank.indeferentno.MonitoringService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class RegisterDto {
    String email;
    String phoneNumber;
    String fullName;
    String passport;
    List<RoleEnum> role;
}
