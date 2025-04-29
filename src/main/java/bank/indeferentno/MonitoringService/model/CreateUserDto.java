package bank.indeferentno.MonitoringService.model;


import java.util.List;

public record CreateUserDto(
        String email,
        String password,
        String phoneNumber,
        String fullName,
        String passport,
        List<RoleEnum> roles
) {
}