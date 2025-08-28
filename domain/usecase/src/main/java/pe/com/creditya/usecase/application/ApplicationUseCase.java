package pe.com.creditya.usecase.application;

import lombok.RequiredArgsConstructor;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.common.exception.UserNotFoundException;
import pe.com.creditya.model.common.utils.DomainErrorMapper;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanStatusRepository loanStatusRepository;

    public Mono<Application> saveLoanApplication(Application application) {
        return userRepository.getUserByDocumentNumber(application.getDocumentNumber())
                .switchIfEmpty(Mono.error(new UserNotFoundException(application.getDocumentNumber())))
                .flatMap(user -> {
                    application.setEmail(user.getEmail());
                    return validLoanType(application.getLoanType())
                            .then(findInitialStatus())
                            .flatMap(initialStatus -> {
                                application.setIdStatus(initialStatus.getId());
                                return applicationRepository.saveLoanApplication(application);
                            });
                }).onErrorResume(error -> Mono.error(DomainErrorMapper.map(error)));
    }

    private Mono<Void> validLoanType(Long localTypeId) {
        return loanTypeRepository.findByIdAndAutomaticValidationTrue(localTypeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Tipo de préstamo no válido o inactivo")))
                .then(Mono.empty());
    }

    private Mono<LoanStatus> findInitialStatus() {
        return loanStatusRepository.findByName("Pendiente de revisión")
                .switchIfEmpty(Mono.error(new RuntimeException("Estado inicial no configurado")));
    }
}



