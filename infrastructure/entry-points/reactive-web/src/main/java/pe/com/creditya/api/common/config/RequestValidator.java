package pe.com.creditya.api.common.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pe.com.creditya.api.common.constant.LogConstant;
import pe.com.creditya.model.common.validations.LoanApplicationValidator;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    @Bean
    public LoanApplicationValidator loanApplicationValidator() {
        return new LoanApplicationValidator();
    }

    public <T> Mono<T> validate(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            log.warn(LogConstant.LOGGER_VALIDATE_FAILURE,
                    request,
                    violations.stream().map(ConstraintViolation::getMessage).toList());
            return Mono.error(new ConstraintViolationException(violations));
        }
        log.info(LogConstant.LOGGER_VALIDATE_SUCCESS, request);
        return Mono.just(request);
    }

}