package pe.com.creditya.usecase.application;

import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.ApplicationReport;
import pe.com.creditya.model.application.PaginatedApplication;
import reactor.core.publisher.Mono;

public interface IApplicationUseCase {
    Mono<Application> saveLoanApplication(Application application,String token);

    Mono<PaginatedApplication<ApplicationReport>> getApplicationByStatusPaged(String status, int page, int size,String token);
}
