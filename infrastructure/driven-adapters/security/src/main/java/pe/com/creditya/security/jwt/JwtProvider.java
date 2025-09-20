package pe.com.creditya.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pe.com.creditya.security.constants.Constants;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final PublicKey publicKey;

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException ex) {
            log.error(Constants.LOG_PARSE_ERROR, ex);
            throw new BadCredentialsException(Constants.LOG_INVALID_TOKEN, ex);
        }
    }

    public Mono<Authentication> getAuthentication(String token) {
        try {
            Claims claims = parseClaims(token);

            String username = claims.getSubject();
            List<String> roles = getRoles(claims);

            List<GrantedAuthority> authorities = roles == null
                    ? List.of()
                    : roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails userDetails = User.withUsername(username)
                    .authorities(authorities)
                    .build();

            return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, token, authorities));
        } catch (BadCredentialsException ex) {
            log.warn(Constants.LOG_INVALID_TOKEN, ex.getMessage());
            return Mono.error(ex);
        } catch (Exception ex) {
            log.error(Constants.LOG_MISSING_JWT_AUTHENTICATION, ex);
            return Mono.error(new BadCredentialsException(Constants.LOG_INVALID_TOKEN, ex));
        }
    }
    private List<String> getRoles(Claims claims) {
        Object roles = claims.get(Constants.CLAIMS_NAME);
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }
        return List.of();
    }
}


