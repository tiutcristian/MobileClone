package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.UserNotFoundException;
import ro.msg.mobile_clone.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(User u) {
        return userRepository.save(u);
    }


    public User getUserById(Long id)
            throws UserNotFoundException {

        return userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);
    }


    public Page<User> getUsersPaginated(int pageNumber, int size) {
        return userRepository.findAll(PageRequest.of(pageNumber, size));
    }


    public User updateUser(Long id, @NotNull User userDto)
            throws UserNotFoundException {

        User user = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());

        return userRepository.save(user);
    }


    public void deleteUser(Long id)
            throws UserNotFoundException {

        User user = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }
}
