package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.entity.validator.UserValidator;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;


    public User createUser(User u) throws UniqueFieldsViolationException {
        userValidator.validateUser(u);
        return userRepository.save(u);
    }


    public User getUserById(Long id)
            throws EntityNotFoundException {

        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }


    public Page<User> getUsersPaginated(int pageNumber, int size) {
        return userRepository.findAll(PageRequest.of(pageNumber, size));
    }


    public User updateUser(Long id, @NotNull User userDto)
            throws EntityNotFoundException, UniqueFieldsViolationException {

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());

        userValidator.validateUser(user);

        return userRepository.save(user);
    }


    public void deleteUser(Long id)
            throws EntityNotFoundException {

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        userRepository.delete(user);
    }
}
