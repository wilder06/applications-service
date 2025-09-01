package pe.com.creditya.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.creditya.consumer.mapper.UserMapper;
import pe.com.creditya.model.common.constants.LoggerConstants;
import pe.com.creditya.model.common.exception.CustomClientException;
import pe.com.creditya.model.user.User;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {
    private final WebClient client;
    private final UserMapper userMapper;
    private static final ParameterizedTypeReference<ErrorResponse<ErrorResponseDto>> GENERIC_RESPONSE = new
            ParameterizedTypeReference<>() {
            };

    @Override
    public Mono<User> getUserByDocumentNumber(String documentNumber) {
        log.info(LoggerConstants.LOGGER_INIT_CONSUME_CLIENT);
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios/{documentNumber}")
                        .build(documentNumber))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(GENERIC_RESPONSE)
                                .doOnNext(error ->
                                        log.error(LoggerConstants.LOGGER_CLIENT_ERROR, error))
                                .flatMap(error -> Mono.error(new CustomClientException(error.toString())))
                )
                .bodyToMono(UserResponse.class)
                .map(userMapper::toUser)
                .doOnSuccess(user -> log.info(LoggerConstants.LOGGER_USER, documentNumber))
                .doOnError(ex -> log.error(LoggerConstants.LOGGER_USER_NOT_FOUND, documentNumber, ex.getMessage()));
    }

}
