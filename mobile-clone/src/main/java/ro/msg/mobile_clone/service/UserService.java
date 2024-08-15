package ro.msg.mobile_clone.service;


import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.repository.UserRepository;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}
