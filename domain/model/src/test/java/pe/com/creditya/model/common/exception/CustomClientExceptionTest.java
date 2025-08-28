package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomClientExceptionTest {

    @Test
    void shouldStoreMessage() {
        // given
        String expectedMessage = "Error en el cliente externo";

        // when
        CustomClientException exception = new CustomClientException(expectedMessage);

        // then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause()); // no hay cause porque el constructor no lo recibe
    }

    @Test
    void shouldThrowCustomClientException() {
        // when & then
        CustomClientException thrown = assertThrows(
                CustomClientException.class,
                () -> { throw new CustomClientException("Fallo en la comunicación"); }
        );

        assertEquals("Fallo en la comunicación", thrown.getMessage());
    }
}