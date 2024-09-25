package ro.msg.mobile_clone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.entity.validator.UserValidator;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock UserValidator userValidator;
    @InjectMocks UserService userService;

    private User user;
    private User user2;
    private Pageable pageable;

    @BeforeEach
    void setupUser() {
        user = new User();
        user.setFirstName("Test First Name");
        user.setLastName("Test Last Name");
        user.setEmail("test@mail.com");
        user.setPhone("0712345678");
    }

    private void setupUser2() {
        user2 = new User();
        user2.setFirstName("Test First Name 2");
        user2.setLastName("Test Last Name 2");
        user2.setEmail("test@mail2.com");
        user2.setPhone("0712345679");
    }

    private void setupPageable() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = null;
        try {
            result = userService.createUser(user);
        } catch (UniqueFieldsViolationException e) {
            fail("UniqueFieldsViolationException thrown incorrectly.");
        }

        assertEquals(user, result);
    }

    @Test
    void testCreateUserThrowsUniqueFieldsViolationException() throws UniqueFieldsViolationException {

        doThrow(new UniqueFieldsViolationException(User.class, Set.of("email")))
                .when(userValidator).validateUser(user);

        assertThrows(
                UniqueFieldsViolationException.class,
                () -> userService.createUser(user)
        );
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = null;
        try {
            result = userService.getUserById(1L);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown incorrectly.");
        }

        assertEquals(user, result);
    }

    @Test
    void testGetUserByIdThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserById(1L)
        );
    }

    @Test
    void testGetAllPaginated() {
        setupPageable();
        when(userRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));

        Page<User> result = userService.getAllPaginated(pageable);

        assertEquals(result.getContent().getFirst(), user);
    }

    @Test
    void testUpdateUser() {
        setupUser2();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = null;
        try {
            result = userService.updateUser(1L, user2);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown incorrectly.");
        } catch (UniqueFieldsViolationException e) {
            fail("UniqueFieldsViolationException thrown incorrectly.");
        }

        assertEquals(user2, result);
    }

    @Test
    void testUpdateUserThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(1L, user)
        );
    }

    @Test
    void testUpdateUserThrowsUniqueFieldsViolationException() throws UniqueFieldsViolationException {
        setupUser2();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doThrow(new UniqueFieldsViolationException(User.class, Set.of("email")))
                .when(userValidator).validateUser(user2);

        assertThrows(
                UniqueFieldsViolationException.class,
                () -> userService.updateUser(1L, user2)
        );
    }

    @Test
    void testDeleteUserValid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUserInvalid() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteUser(1L)
        );
    }
}
