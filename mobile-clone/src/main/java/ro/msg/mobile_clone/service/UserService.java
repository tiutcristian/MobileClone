package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.model.entity.User;
import ro.msg.mobile_clone.model.entity.validator.UserValidator;
import ro.msg.mobile_clone.other.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.other.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;


    public User createUser(User u) throws UniqueFieldsViolationException {
        userValidator.validateUser(u);
        log.debug("User validation passed");

        log.debug("Saving user: {}", u);
        return userRepository.save(u);
    }


    public User getUserById(Long id)
            throws EntityNotFoundException {

        log.debug("Retrieving user with id: {}...", id);
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }


    public Page<User> getAllPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public User updateUser(Long id, @NotNull User u)
            throws EntityNotFoundException, UniqueFieldsViolationException {

        log.debug("Retrieving user with id: {}...", id);
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        log.debug("User retrieved: {}", user);

        user.setFirstName(u.getFirstName());
        log.debug("First name updated to: {}", user.getFirstName());

        user.setLastName(u.getLastName());
        log.debug("Last name updated to: {}", user.getLastName());

        user.setEmail(u.getEmail());
        log.debug("Email updated to: {}", user.getEmail());

        user.setPhone(u.getPhone());
        log.debug("Phone updated to: {}", user.getPhone());

        userValidator.validateUser(user);
        log.debug("User validation passed");

        log.debug("Saving user: {}", user);
        return userRepository.save(user);
    }


    public void deleteUser(Long id)
            throws EntityNotFoundException {

        log.debug("Retrieving user with id: {}...", id);
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        log.debug("User retrieved: {}", user);

        log.debug("Deleting user...");
        userRepository.delete(user);
    }
}
