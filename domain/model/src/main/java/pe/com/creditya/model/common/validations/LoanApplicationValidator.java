package pe.com.creditya.model.common.validations;

import pe.com.creditya.model.application.Application;
public class LoanApplicationValidator {
    public void validate(Application application) {
        LoanApplicationRules.validateAmount(application.getAmount());
        LoanApplicationRules.validateTerm(application.getTerm());
        LoanApplicationRules.validateEmail(application.getEmail());
        LoanApplicationRules.validateStatus(application.getIdStatus());
        LoanApplicationRules.validateLoanType(application.getIdLoanType());
        DocumentNumberValidator.validateDocumentNumber(application.getDocumentNumber());

    }
}
