package ro.msg.mobile_clone.service;

import ro.msg.mobile_clone.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto) throws Exception;

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long id, UserDto userDto) throws Exception;

    void deleteUser(Long id);
}
