package pe.com.creditya.r2dbc.loanStatus;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import pe.com.creditya.model.common.exception.LoanTypePersistenceException;
import pe.com.creditya.model.common.exception.NotFoundException;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.r2dbc.common.constants.LoggerConstants;
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
        return repository.findByName(name)
                .switchIfEmpty(Mono.error(new NotFoundException(
                        LoggerConstants.LOG_NOT_FOUND_LOAN_STATUS + name
                )))
                .onErrorMap(DataAccessException.class, ex ->
                        new LoanTypePersistenceException(
                                LoggerConstants.LOG_VERIFY_EXIST_LOAN_TYPE, ex
                        )
                );
    }
}

