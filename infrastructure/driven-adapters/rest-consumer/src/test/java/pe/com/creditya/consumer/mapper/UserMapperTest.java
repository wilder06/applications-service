package pe.com.creditya.consumer.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pe.com.creditya.consumer.dto.UserResponse;
import pe.com.creditya.model.user.User;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
class UserMapperTest {
private final UserMapper userMapper= Mappers.getMapper(UserMapper.class);

@Test
void shouldMapUserToUserResponse() {
    User user = User.builder()
            .name("87654321")
            .baseSalary(new BigDecimal(2000))
            .email("test@example.com")
            .build();

    UserResponse response = userMapper.toUserResponse(user);

    assertThat(response).isNotNull();
}

@Test
void shouldMapUserResponseToUser() {
    UserResponse response =  UserResponse.builder()
            .name("87654321")
            .email("user@test.com")
            .baseSalary(new BigDecimal(2000))
            .build();

    User user = userMapper.toUser(response);

    assertThat(user).isNotNull();
}
}