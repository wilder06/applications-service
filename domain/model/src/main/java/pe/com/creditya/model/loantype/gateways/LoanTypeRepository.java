package pe.com.creditya.model.loantype.gateways;

import pe.com.creditya.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findByIdAndAutomaticValidationTrue(Long id);
}
