package pe.com.creditya.r2dbc.loanType;

import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.com.creditya.model.common.exception.LoanTypePersistenceException;
import pe.com.creditya.model.common.exception.NotFoundException;
import pe.com.creditya.model.loantype.LoanType;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.r2dbc.common.constants.LoggerConstants;
import pe.com.creditya.r2dbc.entity.LoanTypeEntity;
import pe.com.creditya.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

@Slf4j
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
    public Mono<LoanType> findByIdAndAutomaticValidationTrue(Long idLoanType) {
        log.info(LoggerConstants.LOG_START_VERIFY_EXIST_LOAN_TYPE, idLoanType);

        return repository.findByIdAndAutomaticValidationTrue(idLoanType)
                .switchIfEmpty(Mono.error(new NotFoundException(
                        LoggerConstants.LOG_NOT_FOUND_LOAN_TYPE + idLoanType
                )))
                .onErrorMap(ex -> new LoanTypePersistenceException(
                        LoggerConstants.LOG_VERIFY_EXIST_LOAN_TYPE,
                        ex
                ));
    }

}

