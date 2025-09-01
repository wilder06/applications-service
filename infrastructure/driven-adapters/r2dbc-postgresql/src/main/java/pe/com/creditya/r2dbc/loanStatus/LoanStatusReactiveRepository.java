package pe.com.creditya.r2dbc.loanStatus;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.r2dbc.entity.LoanStatusEntity;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface LoanStatusReactiveRepository extends ReactiveCrudRepository<LoanStatusEntity, Long>, ReactiveQueryByExampleExecutor<LoanStatusEntity> {
    Mono<LoanStatus> findByName(String name);
}
