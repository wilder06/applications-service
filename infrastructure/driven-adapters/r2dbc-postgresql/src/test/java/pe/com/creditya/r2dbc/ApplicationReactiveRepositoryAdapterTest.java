package pe.com.creditya.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.r2dbc.loanApplication.ApplicationReactiveRepository;
import pe.com.creditya.r2dbc.loanApplication.ApplicationReactiveRepositoryAdapter;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @Mock
    ApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    TransactionalOperator transactionalOperator;
    @InjectMocks
    ApplicationReactiveRepositoryAdapter repositoryAdapter;



    @Test
    void mustSaveLoanApplication() {
        Application application = new Application(1, BigDecimal.valueOf(12.1), 12, "12345678", "LoanTest@gmail.com", 1L, 1L);
        ApplicationEntity entity = new ApplicationEntity(1, BigDecimal.valueOf(12.1), 12, "LoanTest@gmail.com", 1L, 1L);
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.map(application, ApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(application);

        Mono<Application> result = repositoryAdapter.saveLoanApplication(application);

        StepVerifier.create(result)
                .expectNext(application)
                .verifyComplete();
    }
}
