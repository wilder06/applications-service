package pe.com.creditya.model.loanstatus.gateways;

import pe.com.creditya.model.loanstatus.LoanStatus;
import reactor.core.publisher.Mono;

public interface LoanStatusRepository {
    Mono<LoanStatus> findByName(String name);
}
