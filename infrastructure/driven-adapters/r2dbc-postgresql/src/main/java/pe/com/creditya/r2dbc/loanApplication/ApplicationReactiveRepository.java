package pe.com.creditya.r2dbc.loanApplication;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Integer>, ReactiveQueryByExampleExecutor<ApplicationEntity> {
    Mono<Long> countByIdStatus(Long status);
    Flux<ApplicationEntity> findByIdStatus(Long idStatus, Pageable pageable);
}
