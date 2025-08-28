package pe.com.creditya.model.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        User user = new User();
        user.setDocumentNumber("12345678");
        user.setEmail("test@example.com");

        assertThat(user.getDocumentNumber()).isEqualTo("12345678");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User("87654321", "other@example.com");

        assertThat(user.getDocumentNumber()).isEqualTo("87654321");
        assertThat(user.getEmail()).isEqualTo("other@example.com");
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .documentNumber("11223344")
                .email("builder@example.com")
                .build();

        assertThat(user.getDocumentNumber()).isEqualTo("11223344");
        assertThat(user.getEmail()).isEqualTo("builder@example.com");
    }

    @Test
    void testToBuilder() {
        User user1 = User.builder()
                .documentNumber("55667788")
                .email("user1@example.com")
                .build();

        User user2 = user1.toBuilder()
                .email("user2@example.com")
                .build();

        assertThat(user2.getDocumentNumber()).isEqualTo("55667788");
        assertThat(user2.getEmail()).isEqualTo("user2@example.com");
    }
}