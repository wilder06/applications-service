package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class NotFoundExceptionTest {
    @Test
    void shouldCreateNotFoundExceptionWithMessage() {
        // Given
        String message = "Recurso no encontrado";

        // When
        NotFoundException exception = new NotFoundException(message);

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Recurso no encontrado");
    }
  
}