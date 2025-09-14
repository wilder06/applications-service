package pe.com.creditya.model.application.gateways;

import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.ApplicationReport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {
    Mono<Application> saveLoanApplication(Application application);

    Mono<Long> countByStatus(Long status);
    Flux<Application> findByStatus(Long status, int offset, int limit);
}
