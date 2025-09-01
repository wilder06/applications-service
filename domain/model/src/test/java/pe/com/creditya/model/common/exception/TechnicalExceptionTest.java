package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TechnicalExceptionTest {

    @Test
    void shouldCreateTechnicalExceptionWithMessageAndCause() {
        // Given
        String message = "Error técnico en la base de datos";
        Throwable cause = new RuntimeException("Connection timeout");

        // When
        TechnicalException exception = new TechnicalException(message, cause);

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error técnico en la base de datos")
                .hasCause(cause);
    }
}
