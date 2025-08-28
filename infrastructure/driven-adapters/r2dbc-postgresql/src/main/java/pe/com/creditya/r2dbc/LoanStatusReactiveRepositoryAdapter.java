package pe.com.creditya.r2dbc;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import pe.com.creditya.r2dbc.entity.LoanStatusEntity;
import pe.com.creditya.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

@Repository
public class LoanStatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanStatus,
        LoanStatusEntity,
        Long,
        LoanStatusReactiveRepository
        > implements LoanStatusRepository {
    public LoanStatusReactiveRepositoryAdapter(LoanStatusReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, LoanStatus.class));

    }

    @Override
    public Mono<LoanStatus> findByName(String name) {
        return repository.findByName(name);
    }
}

