package pe.com.creditya.usecase.application;

import lombok.RequiredArgsConstructor;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.gateways.ApplicationRepository;
import pe.com.creditya.model.application.ApplicationReport;
import pe.com.creditya.model.application.PaginatedApplication;
import pe.com.creditya.model.application.PaginationMetadata;
import pe.com.creditya.model.common.constants.LoggerConstants;
import pe.com.creditya.model.common.exception.ApplicationException;
import pe.com.creditya.model.common.exception.CustomClientException;
import pe.com.creditya.model.common.exception.NotFoundException;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.validations.LoanApplicationValidator;
import pe.com.creditya.model.loanstatus.LoanStatus;
import pe.com.creditya.model.loanstatus.LoanStatusEnum;
import pe.com.creditya.model.loanstatus.gateways.LoanStatusRepository;
import pe.com.creditya.model.loantype.gateways.LoanTypeRepository;
import pe.com.creditya.model.user.User;
import pe.com.creditya.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ApplicationUseCase implements IApplicationUseCase {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final LoanApplicationValidator validator;

    @Override
    public Mono<Application> saveLoanApplication(Application application,String token) {
        return userRepository.getUserByDocumentNumber(application.getDocumentNumber(),token)
                .switchIfEmpty(Mono.error(new NotFoundException(LoggerConstants.USER_NOT_FOUND + application.getDocumentNumber())))
                .flatMap(user -> {
                    application.setEmail(user.getEmail());
                    return validLoanType(application.getIdLoanType())
                            .then(findInitialStatus())
                            .flatMap(initialStatus -> {
                                application.setIdStatus(initialStatus.getId());
                               validator.validate(application);
                                return applicationRepository.saveLoanApplication(application);
                            });
                })
                .onErrorResume(error -> Mono.error(new TechnicalException(LoggerConstants.LOGGER_ERROR_GENERAL + "{}", error)));
    }

    @Override
    public Mono<PaginatedApplication<ApplicationReport>> getApplicationByStatusPaged(String status, int page, int size,String token) {
        long idStatus = LoanStatusEnum.fromName(status);
        int offset = page * size;

        Mono<Long> totalApplicationsMono = applicationRepository.countByStatus(idStatus)
                .onErrorMap(ex -> new TechnicalException("Error obteniendo el total de aplicaciones", ex));


        Mono<List<Application>> applicationsMono = applicationRepository.findByStatus(idStatus, offset, size)
                .collectList().onErrorMap(ex -> new TechnicalException("Error obteniendo la lista de aplicaciones", ex));


        // Obtenemos usuarios a partir de los correos de las aplicaciones
        Mono<Map<String, User>> usersByEmailMono = applicationsMono
                .flatMapMany(Flux::fromIterable)
                .map(Application::getEmail)
                .distinct()
                .collectList()
                .flatMapMany(response->userRepository.getUsersByEmails(response,token))
                .collectMap(User::getEmail)
                .onErrorMap(ex -> new CustomClientException("Error obteniendo usuarios desde el servicio externo", ex));
        ;
        //sirve para combinar varios Mono en uno solo, y esperar a que todos terminen para producir un único resultado (una tupla)
        //totalApplicationsMono → devuelve el total de aplicaciones (un Long).
        //applicationsMono → devuelve la lista de aplicaciones (List<Application>).
        //usersByEmailMono → devuelve un mapa de usuarios (Map<String, User>).
        return Mono.zip(totalApplicationsMono, applicationsMono, usersByEmailMono)
                .map(tuple -> buildPaginatedResponse(tuple.getT1(), tuple.getT2(), tuple.getT3(), page, size))
                .onErrorResume(ex -> Mono.error(new ApplicationException("No se pudo procesar la solicitud", ex)));
    }

    private PaginatedApplication<ApplicationReport> buildPaginatedResponse(
            long total,
            List<Application> applications,
            Map<String, User> usersByEmail,
            int page,
            int size
    ) {
        List<ApplicationReport> reports = applications.stream()
                //mapea usuario con su solicitu por correo
                .map(app -> toApplicationReport(app, usersByEmail.get(app.getEmail())))
                .toList();

        int totalPages = (int) Math.ceil((double) total / size);
        PaginationMetadata meta = new PaginationMetadata(page, size, total, totalPages);

        return new PaginatedApplication<>(reports, meta);
    }

    private ApplicationReport toApplicationReport(Application app, User user) {
        BigDecimal monthly = calculateMonthly(app.getAmount(), app.getTerm());

        return ApplicationReport.builder()
                .amount(app.getAmount())
                .term(app.getTerm())
                .email(app.getEmail())
                .idLoanType(app.getIdLoanType())
                .interestRate(app.getInterestRate())
                .idStatus(app.getIdStatus())
                .baseSalary(user.getBaseSalary())
                .name( user.getName() )
                .monthlyRequestAmount(monthly)
                .build();
    }


    private Mono<Void> validLoanType(Long localTypeId) {
        return loanTypeRepository.findById(localTypeId)
                .switchIfEmpty(Mono.error(new NotFoundException(LoggerConstants.LOGGER_TYPE_LOAN_ERROR)))
                .then(Mono.empty());
    }

    private Mono<LoanStatus> findInitialStatus() {
        return loanStatusRepository.findByName(LoanStatusEnum.PENDING.name())
                .switchIfEmpty(Mono.error(new NotFoundException(LoggerConstants.LOGGER_ERROR_TYPE_STATUS)));
    }

    private BigDecimal calculateMonthly(BigDecimal amount, Integer term) {
        if (term == null || term == 0 || amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.divide(BigDecimal.valueOf(term), RoundingMode.HALF_UP);
    }
}



