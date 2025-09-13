package pe.com.creditya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.com.creditya.consumer.config.VariableClient;
import pe.com.creditya.consumer.dto.ErrorResponse;
import pe.com.creditya.consumer.dto.ErrorResponseDto;
import pe.com.creditya.consumer.dto.UserResponse;
import pe.com.creditya.consumer.exception.AuthenticationException;
import pe.com.creditya.consumer.exception.AuthorizationException;
import pe.com.creditya.consumer.exception.ServiceException;
import pe.com.creditya.consumer.mapper.UserMapper;
import pe.com.creditya.model.common.constants.LoggerConstants;
import pe.com.creditya.model.common.exception.CustomClientException;
import pe.com.creditya.model.user.User;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {

    private final WebClient client;
    private final UserMapper userMapper;
    private final VariableClient variableClient;

    private static final ParameterizedTypeReference<ErrorResponse<ErrorResponseDto>> GENERIC_RESPONSE =
            new ParameterizedTypeReference<>() {};

    @CircuitBreaker(name = "userUsecase", fallbackMethod = "fallbackGetUserByDocument")
    @Override
    public Mono<User> getUserByDocumentNumber(String documentNumber) {
        log.info(LoggerConstants.LOGGER_INIT_CONSUME_CLIENT);

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(variableClient.getPathFindUserByDocumentNumber())
                        .build(documentNumber))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +"")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.FORBIDDEN::equals, this::handleForbiddenError)
                .onStatus(this::isClientError, this::handleClientError)
                .onStatus(this::isServerError, this::handleServerError)
                .bodyToMono(UserResponse.class)
                .map(userMapper::toUser)
                .doOnSuccess(user -> log.info(LoggerConstants.LOGGER_USER, documentNumber))
                .doOnError(ex -> log.error(LoggerConstants.LOGGER_USER_NOT_FOUND, documentNumber, ex.getMessage()))
                .onErrorResume(WebClientResponseException.class,
                        ex -> Mono.error(new ServiceException("User service unavailable: " + ex.getMessage())));
    }

    @CircuitBreaker(name = "userUsecase", fallbackMethod = "fallbackGetUsersByEmails")
    @Override
    public Flux<User> getUsersByEmails(List<String> emails) {
        log.info(LoggerConstants.LOGGER_INIT_CONSUME_CLIENT);

        return client.post()
                .uri(variableClient.getPathFindUsersByEmails())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3anBoQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9BRFZJU09SIl0sInVzZXJJZCI6IjIzNDU2NzgwIiwiaWF0IjoxNzU3Nzc2OTgyLCJleHAiOjE3NTc5NTY5ODJ9.1Ji6hGF1Dcbflik6v1Us6CeDmjgGe7S5qiy9SepOoL8")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(emails)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        response -> Mono.error(new AuthenticationException("Token inválido o expirado")))
                .onStatus(HttpStatus.FORBIDDEN::equals, this::handleForbiddenError)
                .onStatus(this::isClientError, this::handleClientError)
                .onStatus(this::isServerError, this::handleServerError)
                .bodyToFlux(UserResponse.class)
                .map(userMapper::toUser)
                .doOnNext(user -> log.debug(LoggerConstants.LOGGER_USER, user.getEmail()))
                .doOnComplete(() -> log.info(LoggerConstants.LOGGER_SEARCH_COMPLETED, emails.size()))
                .doOnError(ex -> log.error(LoggerConstants.LOGGER_USERS_NOT_FOUND, emails, ex.getMessage()))
                .onErrorResume(WebClientResponseException.class,
                        ex -> Flux.error(new ServiceException("Users service unavailable: " + ex.getMessage())));
    }

    private boolean isClientError(HttpStatusCode status) {
        return status.is4xxClientError()
                && !status.equals(HttpStatus.UNAUTHORIZED)
                && !status.equals(HttpStatus.FORBIDDEN);
    }

    private boolean isServerError(HttpStatusCode status) {
        return status.is5xxServerError();
    }

    private Mono<Throwable> handleClientError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("No error details")
                .doOnNext(errorBody -> log.error("Client error {}: {}", response.statusCode(), errorBody))
                .flatMap(errorBody -> Mono.error(new CustomClientException(
                        String.format("Client error %d: %s", response.statusCode().value(), errorBody)
                )));
    }

    private Mono<Throwable> handleServerError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("No error details")
                .doOnNext(errorBody -> log.error("Server error {}: {}", response.statusCode(), errorBody))
                .flatMap(errorBody -> Mono.error(new ServiceException(
                        String.format("Server error %d: %s", response.statusCode().value(), errorBody)
                )));
    }

    private Mono<Throwable> handleForbiddenError(ClientResponse response) {
        return response.bodyToMono(new ParameterizedTypeReference<ErrorResponse<ErrorResponseDto>>() {})
                .defaultIfEmpty(new ErrorResponse<>(List.of(
                        new ErrorResponseDto("Access denied","Forbidden")
                )))
                .doOnNext(error -> log.warn("Access denied: {}", error.errorResponseDto()))
                .flatMap(error -> Mono.error(new AuthorizationException(
                        "Acceso denegado: " + error.errorResponseDto()
                )));
    }


    private Flux<User> fallbackGetUsersByEmails(List<String> emails, Throwable ex) {
        log.warn("Fallback activated for {} emails. Cause: {}", emails.size(), ex.getMessage());
        return Flux.empty();
    }

    private Mono<User> fallbackGetUserByDocument(String documentNumber, Throwable ex) {
        log.warn("Fallback activated for document {}. Cause: {}", documentNumber, ex.getMessage());
        return Mono.empty();
    }
}

