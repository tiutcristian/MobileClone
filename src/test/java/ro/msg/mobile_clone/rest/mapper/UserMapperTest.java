package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock User user;
    @Mock UserDto userDto;

    @Test
    public void testMapUserToDto() {
        UserDto result = UserMapper.INSTANCE.mapUserToDto(user);

        assertEquals(result.firstName(), user.getFirstName());
        assertEquals(result.lastName(), user.getLastName());
        assertEquals(result.email(), user.getEmail());
        assertEquals(result.phone(), user.getPhone());
    }

    @Test
    public void testMapDtoToUser() {
        User result = UserMapper.INSTANCE.mapDtoToUser(userDto);

        assertEquals(result.getFirstName(), userDto.firstName());
        assertEquals(result.getLastName(), userDto.lastName());
        assertEquals(result.getEmail(), userDto.email());
        assertEquals(result.getPhone(), userDto.phone());
    }
}
