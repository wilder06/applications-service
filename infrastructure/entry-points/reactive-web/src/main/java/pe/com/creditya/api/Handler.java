package pe.com.creditya.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.creditya.api.common.config.RequestValidator;
import pe.com.creditya.api.common.constant.LogConstant;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.mapper.ApplicationMapper;
import pe.com.creditya.usecase.application.IApplicationUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final IApplicationUseCase applicationUseCase;
    private final ApplicationMapper applicationMapper;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> listenSaveLoanApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ApplicationRequest.class)
                .doOnNext(req -> log.info(LogConstant.LOGGER_START, req))
                .flatMap(requestValidator::validate)
                .map(applicationMapper::toApplication)
                .doOnNext(application -> log.debug(LogConstant.LOGGER_MAPPER, application))
                .flatMap(applicationUseCase::saveLoanApplication)
                .doOnNext(application -> log.info(LogConstant.LOGGER_SAVE_SUCCESS, application.getIdApplication()))
                .map(applicationMapper::toApplicationResponse)
                .flatMap(savedApplication -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedApplication))
                .doOnError(ex -> log.error(LogConstant.LOGGER_SAVE_FAILURE, ex.getMessage(), ex));
    }
}
