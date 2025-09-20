package pe.com.creditya.security.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pe.com.creditya.security.constants.Constants;
import pe.com.creditya.security.jwt.JwtProvider;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return extractToken(exchange)
                .flatMap(this::authenticate)
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug(Constants.LOG_MISSING_AUTH_HEADER);
                    return Mono.empty();
                }));
    }

    private Mono<String> extractToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(Constants.BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(Constants.BEARER_PREFIX.length()));
    }

    private Mono<SecurityContext> authenticate(String token) {
        return jwtProvider.getAuthentication(token)
                .map(SecurityContextImpl::new)
                .map(SecurityContext.class::cast)
                .doOnError(e -> log.warn(Constants.LOG_INVALID_TOKEN, e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }
}
