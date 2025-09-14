package pe.com.creditya.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.creditya.api.common.config.RequestValidator;
import pe.com.creditya.api.common.constant.LogConstant;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.mapper.ApplicationMapper;
import pe.com.creditya.model.loanstatus.LoanStatusEnum;
import pe.com.creditya.usecase.application.IApplicationUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final IApplicationUseCase applicationUseCase;
    private final ApplicationMapper applicationMapper;
    private final RequestValidator requestValidator;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Mono<ServerResponse> listenSaveLoanApplication(ServerRequest serverRequest) {
        String token = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);

        return serverRequest.bodyToMono(ApplicationRequest.class)
                .doOnNext(req -> log.info(LogConstant.LOGGER_START, req))
                .flatMap(requestValidator::validate)
                .map(applicationMapper::toApplication)
                .doOnNext(application -> log.debug(LogConstant.LOGGER_MAPPER, application))
                .flatMap(response -> applicationUseCase.saveLoanApplication(response, token))
                .doOnNext(application -> log.info(LogConstant.LOGGER_SAVE_SUCCESS, application.getIdApplication()))
                .map(applicationMapper::toApplicationResponse)
                .flatMap(savedApplication -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedApplication))
                .doOnError(ex -> log.error(LogConstant.LOGGER_SAVE_FAILURE, ex, ex));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADVISOR')")
    public Mono<ServerResponse> listenAllLoanApplicationFindStatusPending(ServerRequest serverRequest) {
        String token = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        String status = serverRequest.queryParam("status").map(String::valueOf).orElse(LoanStatusEnum.PENDING.name());
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        return applicationUseCase.getApplicationByStatusPaged(status, page, size, token)
                .map(applicationMapper::toDto)
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res)
                );
    }

}
