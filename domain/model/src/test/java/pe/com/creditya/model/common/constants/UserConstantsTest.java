package pe.com.creditya.model.common.constants;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserConstantsTest {

    @Test
    void shouldContainExpectedConstantValue() {
        String constantValue = UserConstants.USER_NOT_FOUND;

        assertThat(constantValue)
                .isEqualTo("User not found with documentNumber: ");
    }
}
