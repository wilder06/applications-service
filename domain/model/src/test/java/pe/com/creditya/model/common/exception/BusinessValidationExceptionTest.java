package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessValidationExceptionTest {
    @Test
    void shouldCreateBusinessValidationExceptionWithFieldAndMessage() {
        // Given
        String field = "email";
        String message = "debe ser un formato válido";

        // When
        BusinessValidationException exception = new BusinessValidationException(field, message);

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("email: debe ser un formato válido");

        assertThat(exception.getField()).isEqualTo("email");
    }
}