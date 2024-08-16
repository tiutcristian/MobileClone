package ro.msg.mobile_clone.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.mapper.UserMapper;
import ro.msg.mobile_clone.repository.UserRepository;
import ro.msg.mobile_clone.service.UserService;
import ro.msg.mobile_clone.validator.UserValidator;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;


    @Override
    public UserDto createUser(UserDto userDto) throws Exception {

        User user = UserMapper.mapToUser(userDto);

        userValidator.validate(user);

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }


    @Override
    public UserDto getUserById(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.mapToUserDto(user);
    }


    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }


    @Override
    public UserDto updateUser(Long id, @NotNull UserDto userDto) throws Exception {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }

        userValidator.validate(user);

        User updatedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}
