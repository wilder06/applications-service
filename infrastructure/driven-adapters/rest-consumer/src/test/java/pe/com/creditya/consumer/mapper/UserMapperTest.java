package pe.com.creditya.consumer.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pe.com.creditya.consumer.UserResponse;
import pe.com.creditya.model.user.User;

import static org.assertj.core.api.Assertions.assertThat;
class UserMapperTest {
private final UserMapper userMapper= Mappers.getMapper(UserMapper.class);

@Test
void shouldMapUserToUserResponse() {
    User user = User.builder()
            .documentNumber("12345678")
            .email("test@example.com")
            .build();

    UserResponse response = userMapper.toUserResponse(user);

    assertThat(response).isNotNull();
}

@Test
void shouldMapUserResponseToUser() {
    UserResponse response = new UserResponse();
    response.setDocumentNumber("87654321");
    response.setEmail("user@test.com");

    User user = userMapper.toUser(response);

    assertThat(user).isNotNull();
}
}