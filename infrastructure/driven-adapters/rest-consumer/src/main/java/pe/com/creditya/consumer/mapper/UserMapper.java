package pe.com.creditya.consumer.mapper;

import org.mapstruct.Mapper;
import pe.com.creditya.consumer.UserResponse;
import pe.com.creditya.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    User toUser(UserResponse userResponse);

}
