package pe.com.creditya.r2dbc;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.model.loantype.LoanType;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.r2dbc.entity.LoanStatusEntity;
import pe.com.creditya.r2dbc.entity.LoanTypeEntity;
import pe.com.creditya.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Long,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, LoanType.class));

    }

    @Override
    public Mono<LoanType> findByIdAndAutomaticValidationTrue(Long id) {
        return repository.findByIdAndAutomaticValidationTrue(id);
    }

}

