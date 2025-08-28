package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionTest {

    @Test
    void shouldStoreMessageAndCause() {
        // given
        String expectedMessage = "Error inesperado en la aplicación";
        Throwable expectedCause = new IllegalArgumentException("Dato inválido");

        // when
        ApplicationException exception = new ApplicationException(expectedMessage, expectedCause);

        // then
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    void shouldPropagateCauseMessage() {
        // given
        Throwable cause = new NullPointerException("Valor nulo");

        // when
        ApplicationException exception = new ApplicationException("Wrapper exception", cause);

        // then
        assertTrue(exception.getMessage().contains("Wrapper exception"));
        assertEquals("Valor nulo", exception.getCause().getMessage());
    }
}