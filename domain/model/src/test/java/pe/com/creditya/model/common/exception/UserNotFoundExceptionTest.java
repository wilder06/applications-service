package pe.com.creditya.model.common.exception;

import org.junit.jupiter.api.Test;
import pe.com.creditya.model.common.constants.UserConstants;

import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        // given
        String documentNumber = "12345678";
        String expectedMessage = UserConstants.USER_NOT_FOUND + documentNumber;

        // when
        UserNotFoundException exception = new UserNotFoundException(documentNumber);

        // then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause()); // No debería haber causa asignada
    }

    @Test
    void shouldThrowUserNotFoundException() {
        // given
        String documentNumber = "87654321";

        // when & then
        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> { throw new UserNotFoundException(documentNumber); }
        );

        assertEquals(UserConstants.USER_NOT_FOUND + documentNumber, thrown.getMessage());
    }
}