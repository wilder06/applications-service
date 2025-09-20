package pe.com.creditya.usecase.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.ApplicationReport;
import pe.com.creditya.model.application.PaginatedApplication;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.validations.LoanApplicationValidator;
import pe.com.creditya.model.loanstatus.LoanStatusEnum;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.model.loantype.LoanType;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.model.user.User;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ApplicationUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private LoanTypeRepository loanTypeRepository;
    @Mock
    private LoanStatusRepository loanStatusRepository;
    @Mock
    private LoanApplicationValidator validator;

    private ApplicationUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ApplicationUseCase(userRepository,
                applicationRepository,
                loanTypeRepository,
                loanStatusRepository, validator);
    }

    @Test
    void saveLoanApplication_success() {
        Application application = new Application();
        application.setDocumentNumber("12345678");
        application.setIdLoanType(1L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setName("demo");
        user.setBaseSalary(BigDecimal.valueOf(100.2));

        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setName(LoanStatusEnum.PENDING.name());
        loanStatus.setId(99L);
        loanStatus.setDescription("Pendiente de revisión");

        when(userRepository.getUserByDocumentNumber(any(),any())).thenReturn(Mono.just(user));
        when(loanTypeRepository.findById(1L)).thenReturn(Mono.just(new LoanType()));
        when(loanStatusRepository.findByName(LoanStatusEnum.PENDING.name())).thenReturn(Mono.just(loanStatus));
        when(applicationRepository.saveLoanApplication(any(Application.class))).thenReturn(Mono.just(application));

        Mono<Application> result = useCase.saveLoanApplication(application,"jdfksdlabglkas");

        StepVerifier.create(result)
                .expectNextMatches(app -> app.getEmail().equals("user@test.com"))
                .verifyComplete();
    }

    @Test
    void saveLoanApplication_TechnicalException() {
        Application application = new Application();
        application.setDocumentNumber("90876590");

        when(userRepository.getUserByDocumentNumber(any(),any())).thenReturn(Mono.empty());

        Mono<Application> result = useCase.saveLoanApplication(application,"dgsabvafsb");

        StepVerifier.create(result)
                .expectError(TechnicalException.class)
                .verify();
    }
    @Test
    void shouldReturnPaginatedResponse_whenApplicationsExists() {
        Application application = new Application();
        application.setDocumentNumber("12345678");
        application.setIdLoanType(1L);
        application.setTerm(12);
        application.setIdStatus(1L);
        application.setEmail("user@test.com");
        application.setAmount(BigDecimal.valueOf(1235.12));

        User user = new User();
        user.setEmail("user@test.com");
        user.setName("user");
        user.setBaseSalary(BigDecimal.valueOf(12.5));


        when(applicationRepository.countByStatus(anyLong()))
                .thenReturn(Mono.just(1L));
        when(applicationRepository.findByStatus(anyLong(), anyInt(), anyInt()))
                .thenReturn(Flux.just(application));
        when(userRepository.getUsersByEmails(any(),any()))
                .thenReturn(Flux.just(user));

        Mono<PaginatedApplication<ApplicationReport>> result =
                useCase.getApplicationByStatusPaged(LoanStatusEnum.PENDING.name(), 0, 10,any());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.data()).hasSize(1);
                })
                .verifyComplete();
    }
    @Test
    void shouldReturnPaginatedResponse_whenApplicationsExistsFails() {
        Application application = new Application();
        application.setDocumentNumber("12345678");
        application.setIdLoanType(1L);
        application.setTerm(0);
        application.setIdStatus(1L);
        application.setEmail("user@test.com");
        application.setAmount(BigDecimal.valueOf(0));

        User user = new User();
        user.setEmail("user@test.com");
        user.setName("user");
        user.setBaseSalary(BigDecimal.valueOf(12.5));


        when(applicationRepository.countByStatus(anyLong()))
                .thenReturn(Mono.just(1L));
        when(applicationRepository.findByStatus(anyLong(), anyInt(), anyInt()))
                .thenReturn(Flux.just(application));
        when(userRepository.getUsersByEmails(any(),any()))
                .thenReturn(Flux.just(user));

        Mono<PaginatedApplication<ApplicationReport>> result =
                useCase.getApplicationByStatusPaged(LoanStatusEnum.PENDING.name(), 0, 10,any());

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.data()).hasSize(1);
                })
                .verifyComplete();
    }
}
