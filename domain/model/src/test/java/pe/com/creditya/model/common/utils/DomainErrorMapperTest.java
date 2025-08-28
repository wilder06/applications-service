package pe.com.creditya.model.common.utils;

import org.junit.jupiter.api.Test;
import pe.com.creditya.model.common.exception.ApplicationException;
import pe.com.creditya.model.common.exception.TechnicalException;
import pe.com.creditya.model.common.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DomainErrorMapperTest {

    @Test
    void shouldReturnUserNotFoundExceptionAsIs() {
        // given
        Throwable original = new UserNotFoundException("12345678");

        // when
        Throwable mapped = DomainErrorMapper.map(original);

        // then
        assertSame(original, mapped); // se devuelve el mismo objeto
    }

    @Test
    void shouldMapIllegalArgumentExceptionToTechnicalException() {
        // given
        IllegalArgumentException original = new IllegalArgumentException("invalid");

        // when
        Throwable mapped = DomainErrorMapper.map(original);

        // then
        assertTrue(mapped instanceof TechnicalException);
        assertEquals("Validación de tipo de préstamo inválido", mapped.getMessage());
        assertSame(original, mapped.getCause());
    }

    @Test
    void shouldMapIllegalStateExceptionToTechnicalException() {
        // given
        IllegalStateException original = new IllegalStateException("bad state");

        // when
        Throwable mapped = DomainErrorMapper.map(original);

        // then
        assertTrue(mapped instanceof TechnicalException);
        assertEquals("Estado inicial no configurado", mapped.getMessage());
        assertSame(original, mapped.getCause());
    }

    @Test
    void shouldMapUnknownExceptionToApplicationException() {
        // given
        NullPointerException original = new NullPointerException("null!");

        // when
        Throwable mapped = DomainErrorMapper.map(original);

        // then
        assertTrue(mapped instanceof ApplicationException);
        assertEquals("Error inesperado", mapped.getMessage());
        assertSame(original, mapped.getCause());
    }
}