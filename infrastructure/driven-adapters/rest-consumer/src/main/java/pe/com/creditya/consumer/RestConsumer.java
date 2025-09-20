package pe.com.creditya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.com.creditya.consumer.common.constants.ClientConstants;
import pe.com.creditya.consumer.config.VariableClient;
import pe.com.creditya.consumer.dto.ErrorResponse;
import pe.com.creditya.consumer.dto.ErrorResponseDto;
import pe.com.creditya.consumer.dto.UserResponse;
import pe.com.creditya.consumer.exception.AuthenticationException;
import pe.com.creditya.model.common.exception.AuthorizationException;
import pe.com.creditya.consumer.exception.ServiceException;
import pe.com.creditya.consumer.mapper.UserMapper;
import pe.com.creditya.model.common.constants.LoggerConstants;
import pe.com.creditya.model.common.exception.CustomClientException;
import pe.com.creditya.model.user.User;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {

    private final WebClient client;
    private final UserMapper userMapper;
    private final VariableClient variableClient;
    @Override
    public Mono<User> getUserByDocumentNumber(String documentNumber) {
        log.info(LoggerConstants.LOGGER_INIT_CONSUME_CLIENT);

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> authentication.getCredentials().toString())
                .flatMap(token ->
                        client.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path(variableClient.getPathFindUserByDocumentNumber())
                                        .build(documentNumber))
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .onStatus(HttpStatus.UNAUTHORIZED::equals, this::handleUnauthorizedError)
                                .onStatus(HttpStatus.FORBIDDEN::equals, this::handleForbiddenError)
                                .onStatus(this::isClientError, this::handleClientError)
                                .onStatus(this::isServerError, this::handleServerError)
                                .bodyToMono(UserResponse.class)
                                .map(userMapper::toUser)
                                .doOnNext(user -> log.info(LoggerConstants.LOGGER_USER, documentNumber))
                                .onErrorResume(WebClientResponseException.class,
                                        ex -> Mono.error(new ServiceException(
                                                String.format(ClientConstants.ERROR_USER_SERVICE_UNAVAILABLE, ex.getMessage())
                                        )))
                );
    }

    @CircuitBreaker(name = "getUsersByEmails", fallbackMethod = "fallbackGetUsersByEmails")
    @Override
    public Flux<User> getUsersByEmails(List<String> emails) {
        log.info(LoggerConstants.LOGGER_INIT_CONSUME_CLIENTS);

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> authentication.getCredentials().toString())
                .flatMapMany(token ->
                        client.post()
                                .uri(variableClient.getPathFindUsersByEmails())
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(emails)
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .onStatus(HttpStatus.UNAUTHORIZED::equals, this::handleUnauthorizedError)
                                .onStatus(HttpStatus.FORBIDDEN::equals, this::handleForbiddenError)
                                .onStatus(this::isClientError, this::handleClientError)
                                .onStatus(this::isServerError, this::handleServerError)
                                .bodyToFlux(UserResponse.class)
                                .map(userMapper::toUser)
                                .doOnNext(user -> log.debug(LoggerConstants.LOGGER_USER, user.getEmail()))
                                .onErrorResume(WebClientResponseException.class,
                                        ex -> Flux.error(new ServiceException(
                                                String.format(ClientConstants.ERROR_USERS_SERVICE_UNAVAILABLE, ex.getMessage())
                                        )))
                );
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
                .defaultIfEmpty(ClientConstants.ERROR_NO_ERROR_DETAILS)
                .doOnNext(errorBody -> log.error(ClientConstants.LOGGER_CLIENT_ERROR, response.statusCode(), errorBody))
                .flatMap(errorBody -> Mono.error(new CustomClientException(
                        String.format(ClientConstants.ERROR_CLIENT_FORMAT, response.statusCode().value(), errorBody)
                )));
    }

    private Mono<Throwable> handleServerError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty(ClientConstants.ERROR_NO_ERROR_DETAILS)
                .doOnNext(errorBody -> log.error(ClientConstants.LOGGER_SERVER_ERROR, response.statusCode(), errorBody))
                .flatMap(errorBody -> Mono.error(new ServiceException(
                        String.format(ClientConstants.ERROR_SERVER_FORMAT, response.statusCode().value(), errorBody)
                )));
    }

    private Mono<Throwable> handleForbiddenError(ClientResponse response) {
        return response.bodyToMono(new ParameterizedTypeReference<ErrorResponse<ErrorResponseDto>>() {
                })
                .defaultIfEmpty(new ErrorResponse<>(List.of(
                        new ErrorResponseDto(ClientConstants.ERROR_ACCESS_DENIED, ClientConstants.LOGGER_FORBIDDEN)
                )))
                .doOnNext(error -> log.warn(ClientConstants.LOGGER_FORBIDDEN, error.errorResponseDto()))
                .flatMap(error -> Mono.error(new AuthorizationException(
                        ClientConstants.ERROR_ACCESS_DENIED + ": " + error.errorResponseDto()
                )));
    }

    private Mono<Throwable> handleUnauthorizedError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty(ClientConstants.ERROR_TOKEN_INVALID)
                .doOnNext(errorBody -> log.warn(ClientConstants.LOGGER_UNAUTHORIZED, errorBody))
                .flatMap(errorBody -> Mono.error(new AuthenticationException(ClientConstants.ERROR_TOKEN_INVALID)));
    }

    private Flux<User> fallbackGetUsersByEmails(List<String> emails, String token, Throwable ex) {
        log.warn(ClientConstants.LOGGER_FALLBACK_USERS, emails.size(), ex.getMessage());
        return Flux.empty();
    }

    private Mono<User> fallbackGetUserByDocument(String documentNumber, String token, Throwable ex) {
        log.warn(ClientConstants.LOGGER_FALLBACK_USER, documentNumber, ex.getMessage());
        return Mono.empty();
    }
}

