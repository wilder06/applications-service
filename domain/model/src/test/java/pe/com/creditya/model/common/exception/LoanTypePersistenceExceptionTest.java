package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class LoanTypePersistenceExceptionTest {
    @Test
    void shouldCreateLoanTypePersistenceExceptionWithMessage() {
        // Given
        String message = "Error en buscar loan type";
        Throwable cause = new RuntimeException("Error original");

        // When
        LoanTypePersistenceException exception = new LoanTypePersistenceException(message,cause);

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error en buscar loan type");
    }
}