package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock User user;
    @Mock UserDto userDto;

    @Test
    public void testMapUserToDto() {
        UserDto result = UserMapper.INSTANCE.mapUserToDto(user);

        Assertions.assertEquals(result.firstName(), user.getFirstName());
        Assertions.assertEquals(result.lastName(), user.getLastName());
        Assertions.assertEquals(result.email(), user.getEmail());
        Assertions.assertEquals(result.phone(), user.getPhone());
    }

    @Test
    public void testMapDtoToUser() {
        User result = UserMapper.INSTANCE.mapDtoToUser(userDto);

        Assertions.assertEquals(result.getFirstName(), userDto.firstName());
        Assertions.assertEquals(result.getLastName(), userDto.lastName());
        Assertions.assertEquals(result.getEmail(), userDto.email());
        Assertions.assertEquals(result.getPhone(), userDto.phone());
    }
}
