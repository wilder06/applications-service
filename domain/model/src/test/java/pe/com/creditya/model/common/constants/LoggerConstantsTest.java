package pe.com.creditya.model.common.constants;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoggerConstantsTest {

    @Test
    void shouldContainExpectedConstantValue() {
        String constantValue = LoggerConstants.USER_NOT_FOUND;

        assertThat(constantValue)
                .isEqualTo("No se encontro usuario con numero documento: ");
    }
}
