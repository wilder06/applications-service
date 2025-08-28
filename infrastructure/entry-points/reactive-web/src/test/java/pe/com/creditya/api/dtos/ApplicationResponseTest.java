package pe.com.creditya.api.dtos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationResponseTest {

    @Test
    void shouldBuildApplicationResponseCorrectly() {
        // Act
        ApplicationResponse response = ApplicationResponse.builder()
                .amount(BigDecimal.valueOf(5000))
                .term(12)
                .loanType("Personal Loan")
                .build();

        // Assert
        assertThat(response.amount()).isEqualByComparingTo(BigDecimal.valueOf(5000));
        assertThat(response.term()).isEqualTo(12);
        assertThat(response.loanType()).isEqualTo("Personal Loan");
    }

    @Test
    void shouldBeImmutableAndEqual() {
        // Arrange
        ApplicationResponse response1 = ApplicationResponse.builder()
                .amount(BigDecimal.valueOf(3000))
                .term(6)
                .loanType("Hipotecario")
                .build();

        ApplicationResponse response2 = ApplicationResponse.builder()
                .amount(BigDecimal.valueOf(3000))
                .term(6)
                .loanType("Hipotecario")
                .build();

        // Assert
        assertThat(response1).isEqualTo(response2);
    }
}
