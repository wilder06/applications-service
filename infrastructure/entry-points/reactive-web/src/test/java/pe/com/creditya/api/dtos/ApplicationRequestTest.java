package pe.com.creditya.api.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithValidValues() {
        ApplicationRequest request = new ApplicationRequest();
        request.setAmount(BigDecimal.valueOf(1000));
        request.setTerm(12);
        request.setDocumentNumber("12345678");
        request.setLoanType(1L);

        Set<ConstraintViolation<ApplicationRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenAmountIsNull() {
        ApplicationRequest request = new ApplicationRequest();
        request.setAmount(null);
        request.setTerm(12);
        request.setDocumentNumber("12345678");
        request.setLoanType(1L);

        Set<ConstraintViolation<ApplicationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("El monto no puede estar nulo");
    }

    @Test
    void shouldFailValidationWhenAmountIsNegative() {
        ApplicationRequest request = new ApplicationRequest();
        request.setAmount(BigDecimal.valueOf(-10));
        request.setTerm(12);
        request.setDocumentNumber("12345678");
        request.setLoanType(1L);

        Set<ConstraintViolation<ApplicationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("El monto no puede ser menor que 0");
    }

    @Test
    void shouldFailValidationWhenDocumentNumberIsBlank() {
        ApplicationRequest request = new ApplicationRequest();
        request.setAmount(BigDecimal.valueOf(1000));
        request.setTerm(12);
        request.setDocumentNumber("   ");
        request.setLoanType(1L);

        Set<ConstraintViolation<ApplicationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("El Numero de Documento no puede estar vacío");
    }

    @Test
    void shouldFailValidationWhenLoanTypeIsNull() {
        ApplicationRequest request = new ApplicationRequest();
        request.setAmount(BigDecimal.valueOf(1000));
        request.setTerm(12);
        request.setDocumentNumber("12345678");
        request.setLoanType(null);

        Set<ConstraintViolation<ApplicationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("El Tipo de Prestamo no puede estar vacío");
    }
}