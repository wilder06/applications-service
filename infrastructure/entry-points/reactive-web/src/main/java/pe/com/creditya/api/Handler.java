package pe.com.creditya.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.creditya.api.config.RequestValidator;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.mapper.ApplicationMapper;
import pe.com.creditya.usecase.application.ApplicationUseCase;
import reactor.core.publisher.Mono;
@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final ApplicationUseCase applicationUseCase;
    private final ApplicationMapper applicationMapper;
    private final RequestValidator requestValidator;
    public Mono<ServerResponse> listenSaveLoanApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ApplicationRequest.class)
                .doOnNext(req -> log.info("Request recibido para registrar Solicitud: {}", req.getDocumentNumber()))
                .flatMap(requestValidator::validate)
                .map(applicationMapper::toApplication)
                .doOnNext(application -> log.debug("Transformando ApplicationRequest a Application: {}", application))
                .flatMap(applicationUseCase::saveLoanApplication)
                .doOnNext(application -> log.info("Solicitud registrado con éxito: {}", application.getIdApplication()))
                .map(applicationMapper::toApplicationResponse)
                .flatMap(savedApplication -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedApplication))
                .doOnError(ex -> log.error("Error al registrar Solicitud: {}", ex.getMessage(), ex));
    }
}
