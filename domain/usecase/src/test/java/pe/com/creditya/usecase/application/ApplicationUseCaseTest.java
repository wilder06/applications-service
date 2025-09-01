package pe.com.creditya.usecase.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.validations.LoanApplicationValidator;
import pe.com.creditya.model.loanstatus.LoanStatuEnum;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.model.loantype.LoanType;
import pe.com.creditya.model.loantype.LoanTypeEnum;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.model.user.User;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
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

        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setName(LoanStatuEnum.PENDING.name());
        loanStatus.setId(99L);
        loanStatus.setName("Pendiente de revisión");

        when(userRepository.getUserByDocumentNumber("12345678")).thenReturn(Mono.just(user));
        when(loanTypeRepository.findByIdAndAutomaticValidationTrue(1L)).thenReturn(Mono.just(new LoanType()));
        when(loanStatusRepository.findByName(LoanStatuEnum.PENDING.name())).thenReturn(Mono.just(loanStatus));
        when(applicationRepository.saveLoanApplication(any(Application.class))).thenReturn(Mono.just(application));

        Mono<Application> result = useCase.saveLoanApplication(application);

        StepVerifier.create(result)
                .expectNextMatches(app -> app.getEmail().equals("user@test.com") && app.getIdStatus().equals(99L))
                .verifyComplete();
    }

    @Test
    void saveLoanApplication_TechnicalException() {
        Application application = new Application();
        application.setDocumentNumber("90876590");

        when(userRepository.getUserByDocumentNumber("90876590")).thenReturn(Mono.empty());

        Mono<Application> result = useCase.saveLoanApplication(application);

        StepVerifier.create(result)
                .expectError(TechnicalException.class)
                .verify();
    }
}
