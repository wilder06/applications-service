package pe.com.creditya.model.loantype;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class LoanTypeTest {

    @Test
    void testSettersAndGetters() {
        LoanType loanType = new LoanType();

        loanType.setName("Personal Loan");
        loanType.setMinAmount(new BigDecimal("1000.00"));
        loanType.setMaxAmount(new BigDecimal("5000.00"));
        loanType.setInterestRate(12);
        loanType.setAutomaticValidation(true);

        assertThat(loanType.getName()).isEqualTo("Personal Loan");
        assertThat(loanType.getMinAmount()).isEqualByComparingTo("1000.00");
        assertThat(loanType.getMaxAmount()).isEqualByComparingTo("5000.00");
        assertThat(loanType.getInterestRate()).isEqualTo(12);
        assertThat(loanType.getAutomaticValidation()).isTrue();
    }

    @Test
    void testObjectCreation() {
        LoanType loanType = new LoanType();
        assertThat(loanType).isNotNull();
    }
}