package pe.com.creditya.r2dbc.loanApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.ApplicationReport;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.common.exception.ApplicationException;
import pe.com.creditya.r2dbc.common.constants.LoggerConstants;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import pe.com.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class ApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Application,
        ApplicationEntity,
        Integer,
        ApplicationReactiveRepository
        > implements ApplicationRepository {
    public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, Application.class));
        this.transactionalOperator = transactionalOperator;
    }

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Application> saveLoanApplication(Application application) {
        log.info(LoggerConstants.LOG_SAVE_APPLICATION, application);
        return super.save(application)
                .as(transactionalOperator::transactional)
                .onErrorResume(ex ->
                        Mono.error(new ApplicationException(
                                LoggerConstants.LOG_SAVE_APPLICATION_FAIL + application.getDocumentNumber(),
                                ex
                        )));
    }

    @Override
    public Mono<Long> countByStatus(Long status) {
        return repository.countByIdStatus(status);
    }

    @Override
    public Flux<Application> findByStatus(Long status, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return repository.findByIdStatus(status, pageable).map(this::toEntity);
    }
}

