package bank.indeferentno.MonitoringService.config;

import bank.indeferentno.MonitoringService.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @SneakyThrows
    public UUID getUserIdFromAuthentication(Authentication authentication) {
        UUID userId;
        try {
            userId = UUID.fromString(authentication.getName());
        } catch (Exception e) {
            throw new UnauthorizedException("Authentication not contain userId");
        }
        return userId;
    }

    public UUID getUserIdFromToken(String token) {
        String userId = getAllClaimsFromToken(token).get("userId", String.class);
        return UUID.fromString(userId);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

