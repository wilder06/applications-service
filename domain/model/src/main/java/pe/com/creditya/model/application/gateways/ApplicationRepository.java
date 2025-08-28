package pe.com.creditya.model.application.gateways;

import pe.com.creditya.model.application.Application;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {
    Mono<Application> saveLoanApplication(Application application);

}
