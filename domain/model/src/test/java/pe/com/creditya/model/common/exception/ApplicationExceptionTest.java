package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionTest {
    @Test
    void shouldCreateApplicationExceptionWithMessageAndCause() {
        // Given
        String message = "Error en la aplicación";
        Throwable cause = new RuntimeException("Error original");

        // When
        ApplicationException exception = new ApplicationException(message, cause);

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error en la aplicación")
                .hasCause(cause);
    }
}