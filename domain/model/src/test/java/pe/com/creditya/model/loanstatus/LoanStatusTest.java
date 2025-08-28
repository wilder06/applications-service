package pe.com.creditya.model.loanstatus;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoanStatusTest {

    @Test
    void testSettersAndGetters() {
        LoanStatus loanStatus = new LoanStatus();

        loanStatus.setId(1L);
        loanStatus.setName("Approved");
        loanStatus.setDescription("Loan approved successfully");

        assertThat(loanStatus.getId()).isEqualTo(1L);
        assertThat(loanStatus.getName()).isEqualTo("Approved");
        assertThat(loanStatus.getDescription()).isEqualTo("Loan approved successfully");
    }

    @Test
    void testConstructorAndValues() {
        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setId(2L);
        loanStatus.setName("Pending");
        loanStatus.setDescription("Waiting for approval");

        assertThat(loanStatus).isNotNull();
        assertThat(loanStatus.getId()).isEqualTo(2L);
        assertThat(loanStatus.getName()).isEqualTo("Pending");
        assertThat(loanStatus.getDescription()).isEqualTo("Waiting for approval");
    }
}