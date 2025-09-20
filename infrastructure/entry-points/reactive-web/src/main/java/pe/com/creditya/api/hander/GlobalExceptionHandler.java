package pe.com.creditya.api.hander;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import pe.com.creditya.api.common.constant.LogConstant;
import pe.com.creditya.api.dtos.ErrorResponseDto;
import pe.com.creditya.model.common.exception.AuthorizationException;
import pe.com.creditya.model.common.exception.NotFoundException;
import pe.com.creditya.model.common.exception.TechnicalException;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Mono<ResponseEntity<ErrorResponseDto>> buildError(HttpStatus status, String message) {
        return Mono.just(ResponseEntity.status(status)
                .body(new ErrorResponseDto(status.toString(), message, Instant.now())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleValidation(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        return buildError(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleExists(NotFoundException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(TechnicalException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleTechnical(TechnicalException ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleBadCredentials(BadCredentialsException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleAccessDenied(AccessDeniedException ex) {
        return buildError(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleJwt(io.jsonwebtoken.JwtException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid or expired JWT token");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleNoResourceFound(
            NoResourceFoundException ex,
            ServerWebExchange exchange) {

        String path = exchange.getRequest().getPath().value();
        return buildError(HttpStatus.NOT_FOUND, "Resource not found: " + path);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDto>> fallback(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
    }
    @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication error: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(
                        HttpStatus.UNAUTHORIZED.toString(),
                        "El token es inválido o ha expirado"
                )));
    }

    @ExceptionHandler(AuthorizationException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleAuthorizationException(AuthorizationException ex) {
        log.warn("Authorization error: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(
                        HttpStatus.FORBIDDEN.toString(),
                        "No tiene permisos para acceder a este recurso"
                )));
    }
}
