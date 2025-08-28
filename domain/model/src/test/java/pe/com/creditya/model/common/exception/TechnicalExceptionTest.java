package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TechnicalExceptionTest {

    @Test
    void shouldStoreMessageAndCause() {
        String expectedMessage = "Error técnico en el sistema";
        Throwable expectedCause = new RuntimeException("Causa raíz");

        TechnicalException exception = new TechnicalException(expectedMessage, expectedCause);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    void shouldThrowTechnicalException() {
        Throwable cause = new IllegalStateException("Estado inválido");

        TechnicalException thrown = assertThrows(
                TechnicalException.class,
                () -> {
                    throw new TechnicalException("Error al procesar", cause);
                }
        );

        assertEquals("Error al procesar", thrown.getMessage());
        assertEquals(cause, thrown.getCause());
    }
}