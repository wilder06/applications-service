package pe.com.creditya.r2dbc;

import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import pe.com.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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
        ApplicationEntity entity = mapper.map(application, ApplicationEntity.class);

        return repository.save(entity)
                .map(save -> mapper.map(save, Application.class))
                .as(transactionalOperator::transactional);
    }
}

