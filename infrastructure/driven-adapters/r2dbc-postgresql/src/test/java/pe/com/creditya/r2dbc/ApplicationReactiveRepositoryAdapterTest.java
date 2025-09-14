package pe.com.creditya.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.reactive.TransactionalOperator;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.loanstatus.LoanStatusEnum;
import pe.com.creditya.r2dbc.loanApplication.ApplicationReactiveRepository;
import pe.com.creditya.r2dbc.loanApplication.ApplicationReactiveRepositoryAdapter;
import pe.com.creditya.r2dbc.entity.ApplicationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.*;
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
        Application application = new Application(1, BigDecimal.valueOf(12.1), 12, "12345678", "LoanTest@gmail.com", 1L, 1L, new BigDecimal("12.0"));
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

    @Test
    void mustCountByStatus() {

        when(repository.countByIdStatus(LoanStatusEnum.PENDING.getId())).thenReturn(Mono.just(12L));
        Mono<Long> result = repositoryAdapter.countByStatus(LoanStatusEnum.PENDING.getId());
        StepVerifier.create(result)
                .expectNext(12L)
                .verifyComplete();
    }
    @Test
    void findByStatus_returnsApplications() {

        // given
        ApplicationEntity entity1 = new ApplicationEntity(1, BigDecimal.valueOf(12.1), 12, "LoanTest@gmail.com", 1L, 1L);
        ApplicationEntity entity2 = new ApplicationEntity(2, BigDecimal.valueOf(12.1), 12, "LoanTest@gmail.com", 1L, 1L);

        Application mappedApp1 = new Application(1, BigDecimal.valueOf(12.1), 12, "12345678", "LoanTest@gmail.com", 1L, 1L, new BigDecimal("12.0"));
        Application mappedApp2 = new Application(2, BigDecimal.valueOf(12.1), 12, "12345678", "LoanTest@gmail.com", 1L, 1L, new BigDecimal("12.0"));

        when(repository.findByIdStatus(eq(1L), any(Pageable.class)))
                .thenReturn(Flux.just(entity1, entity2));
        when(mapper.map(entity1, Application.class)).thenReturn(mappedApp1);
        when(mapper.map(entity2, Application.class)).thenReturn(mappedApp2);

        // when
        Flux<Application> result = repositoryAdapter.findByStatus(1L, 0, 2);

        // then
        StepVerifier.create(result)
                .expectNext(mappedApp1)
                .expectNext(mappedApp2)
                .verifyComplete();
    }
}
