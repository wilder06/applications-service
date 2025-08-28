package pe.com.creditya.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.creditya.consumer.mapper.UserMapper;
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
        log.info("Iniciando la validacion del usuario");
        return client
                .get()
                .uri("/api/v1/usuarios/{documentNumber}", documentNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(GENERIC_RESPONSE)
                                .doOnNext(error -> log.error("Error from client API: {}", error))
                                .flatMap(error -> Mono.error(new CustomClientException(error.toString())))
                )
                .bodyToMono(UserResponse.class)
                .map(userMapper::toUser)
                .doOnSuccess(user -> log.info("Se encontro el cliente con Numero de Documento: {}", documentNumber))
                .doOnError(ex -> log.error("No se encontro el cliente {}: {}", documentNumber, ex.getMessage()));
    }


    private Mono<User> getUserByDocumentNumberFallback(String documentNumber, Throwable ex) {
        return Mono.error(new RuntimeException("Fallback: El servicio User service no esta disponible", ex));
    }
}
