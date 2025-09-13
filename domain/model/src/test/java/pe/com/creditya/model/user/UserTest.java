package pe.com.creditya.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        User user = new User();
        user.setEmail("test@example.com");

        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User("other@example.com","other",new BigDecimal("132.2"));

        assertThat(user.getEmail()).isEqualTo("other@example.com");
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .email("builder@example.com")
                .build();

        assertThat(user.getEmail()).isEqualTo("builder@example.com");
    }

    @Test
    void testToBuilder() {
        User user1 = User.builder()
                .email("user1@example.com")
                .build();

        User user2 = user1.toBuilder()
                .email("user2@example.com")
                .build();

        assertThat(user2.getEmail()).isEqualTo("user2@example.com");
    }
}