package pe.com.creditya.usecase.application;

import pe.com.creditya.model.application.Application;
import reactor.core.publisher.Mono;

public interface IApplicationUseCase {
    Mono<Application> saveLoanApplication(Application application);
}
