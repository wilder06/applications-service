package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomClientExceptionTest {

    @Test
    void shouldCreateCustomClientExceptionWithMessage() {
        // Given
        String message = "Error en cliente externo";

        // When
        CustomClientException exception = new CustomClientException(message);

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error en cliente externo");
    }
}