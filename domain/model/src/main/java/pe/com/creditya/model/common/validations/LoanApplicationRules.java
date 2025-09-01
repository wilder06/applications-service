package pe.com.creditya.model.common.validations;

import pe.com.creditya.model.common.exception.BusinessValidationException;

import java.math.BigDecimal;

public class LoanApplicationRules {
    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException("amount", "debe ser mayor a cero");
        }
    }

    public static void validateTerm(Integer term) {
        if (term == null || term <= 0) {
            throw new BusinessValidationException("term", "debe ser mayor a cero");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new BusinessValidationException("email", "formato inválido");
        }
    }

    public static void validateStatus(Long idStatus) {
        if (idStatus == null || idStatus <= 0) {
            throw new BusinessValidationException("idStatus", "debe ser válido");
        }
    }

    public static void validateLoanType(Long idLoanType) {
        if (idLoanType == null || idLoanType <= 0) {
            throw new BusinessValidationException("loanType", "debe ser válido");
        }
    }
}
