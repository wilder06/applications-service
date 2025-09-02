package pe.com.creditya.usecase.application;

import lombok.RequiredArgsConstructor;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.common.constants.LoggerConstants;
import pe.com.creditya.model.common.exception.NotFoundException;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.validations.LoanApplicationValidator;
import pe.com.creditya.model.loanstatus.LoanStatuEnum;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplicationUseCase implements IApplicationUseCase {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final LoanApplicationValidator validator;

    @Override
    public Mono<Application> saveLoanApplication(Application application) {
        return userRepository.getUserByDocumentNumber(application.getDocumentNumber())
                .switchIfEmpty(Mono.error(new NotFoundException(LoggerConstants.USER_NOT_FOUND+application.getDocumentNumber())))
                .flatMap(user -> {
                    application.setEmail(user.getEmail());
                    return validLoanType(application.getIdLoanType())
                            .then(findInitialStatus())
                            .flatMap(initialStatus -> {
                                application.setIdStatus(initialStatus.getId());
                                validator.validate(application);
                                return applicationRepository.saveLoanApplication(application);
                            });
                }).onErrorResume(error -> Mono.error(new TechnicalException(LoggerConstants.LOGGER_ERROR_GENERAL+"{}",error)));
    }

    private Mono<Void> validLoanType(Long localTypeId) {
        return loanTypeRepository.findByIdAndAutomaticValidationTrue(localTypeId)
                .switchIfEmpty(Mono.error(new NotFoundException(LoggerConstants.LOGGER_TYPE_LOAN_ERROR)))
                .then(Mono.empty());
    }

    private Mono<LoanStatus> findInitialStatus() {
        return loanStatusRepository.findByName(LoanStatuEnum.PENDING.name())
                .switchIfEmpty(Mono.error(new NotFoundException(LoggerConstants.LOGGER_ERROR_TYPE_STATUS)));
    }
}



