package ro.msg.mobile_clone.mapper;

import org.jetbrains.annotations.NotNull;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;

public class UserMapper {

    public static @NotNull User mapToUser(@NotNull UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEmail(userDto.email());
        user.setPhone(userDto.phone());
        return user;
    }

    public static @NotNull UserDto mapToUserDto(@NotNull User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
