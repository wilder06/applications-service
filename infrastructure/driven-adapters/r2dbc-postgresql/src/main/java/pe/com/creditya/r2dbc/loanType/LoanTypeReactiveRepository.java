package pe.com.creditya.r2dbc.loanType;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.creditya.model.loantype.LoanType;
import pe.com.creditya.r2dbc.entity.LoanTypeEntity;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, Long>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    Mono<LoanType> findByIdAndAutomaticValidationTrue(Long id);
}
