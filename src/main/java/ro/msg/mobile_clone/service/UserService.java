package ro.msg.mobile_clone.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.entity.validator.UserValidator;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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

    public User findByEmail(String email) {
        log.debug("Searching user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("No user found with email: {}", email);
        } else {
            log.debug("User found: {}", user);
        }
        return user;
    }

    public char[] getSecretByEmail(String email) {
        log.debug("Retrieving 2FA secret for user with email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("No user found with email: {}", email);
            return null;
        }
        String secret = user.getSecret2FA();
        log.debug("2FA secret retrieved: {}", secret);
        return secret != null ? secret.toCharArray() : null;
    }

    public void update2FASecret(String username, String secret) {
        log.debug("Updating 2FA secret for user: {}", username);
        User user = userRepository.findByEmail(username);
        if (user == null) {
            log.warn("No user found with email: {}", username);
            return;
        }
        user.setSecret2FA(secret);
        log.debug("2FA secret updated to: {}", secret);
        userRepository.save(user);
    }

    public void enableTwoFA(String username) {
    log.debug("Enabling 2FA for user: {}", username);
        User user = userRepository.findByEmail(username);
        if (user == null) {
            log.warn("No user found with email: {}", username);
            return;
        }
        user.set2FAEnabled(true);
        log.debug("2FA enabled for user: {}", username);
        userRepository.save(user);
    }
}
