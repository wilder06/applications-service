package pe.com.creditya.model.application;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    @Test
    void testSettersAndGetters() {
        Application application = new Application();

        application.setIdApplication(1);
        application.setAmount(BigDecimal.valueOf(1000.50));
        application.setTerm(12);
        application.setDocumentNumber("12345678");
        application.setEmail("test@mail.com");
        application.setIdStatus(10L);
        application.setLoanType(5L);

        assertThat(application.getIdApplication()).isEqualTo(1);
        assertThat(application.getAmount()).isEqualTo(BigDecimal.valueOf(1000.50));
        assertThat(application.getTerm()).isEqualTo(12);
        assertThat(application.getDocumentNumber()).isEqualTo("12345678");
        assertThat(application.getEmail()).isEqualTo("test@mail.com");
        assertThat(application.getIdStatus()).isEqualTo(10L);
        assertThat(application.getLoanType()).isEqualTo(5L);
    }

    @Test
    void testConstructorAllArgs() {
        Application application = new Application();
        application.setIdApplication(2);
        application.setAmount(BigDecimal.valueOf(5000));
        application.setTerm(24);

        assertThat(application.getIdApplication()).isEqualTo(2);
        assertThat(application.getAmount()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(application.getTerm()).isEqualTo(24);
    }
}