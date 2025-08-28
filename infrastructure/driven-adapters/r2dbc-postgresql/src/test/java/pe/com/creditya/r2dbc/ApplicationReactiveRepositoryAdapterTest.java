package pe.com.creditya.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @Mock
    private ApplicationReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator transactionalOperator;

    private ApplicationReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ApplicationReactiveRepositoryAdapter(repository, mapper, transactionalOperator);
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void mustSaveLoanApplication() {
        Application application = new Application(1, BigDecimal.valueOf(12.1), 12, "12345678", "LoanTest@gmail.com", 1L, 1L);
        ApplicationEntity entity = new ApplicationEntity(1, BigDecimal.valueOf(12.1), 12, "LoanTest@gmail.com", 1L, 1L);

        when(mapper.map(application, ApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(application);

        Mono<Application> result = adapter.saveLoanApplication(application);

        StepVerifier.create(result)
                .expectNextMatches(app -> app.getDocumentNumber().equals("12345678"))
                .verifyComplete();
    }
}
