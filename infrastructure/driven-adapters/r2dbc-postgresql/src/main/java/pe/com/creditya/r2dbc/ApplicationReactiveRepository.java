package pe.com.creditya.r2dbc;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;

// TODO: This file is just an example, you should delete or modify it
public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Integer>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

}
