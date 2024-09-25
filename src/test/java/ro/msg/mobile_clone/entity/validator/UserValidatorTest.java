package ro.msg.mobile_clone.entity.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private User user;

    @Mock
    private User user2;

    private void setup(User foundByEmail, User foundByPhone, Boolean differentIds) {
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(foundByEmail);
        Mockito.when(userRepository.findByPhone(user.getPhone()))
                .thenReturn(foundByPhone);

        if (differentIds != null) {
            Mockito.when(user.getId()).thenReturn(1L);
            Mockito.when(user2.getId()).thenReturn(differentIds ? 2L : 1L);
        }
    }

    @Test
    public void testValidateUser() {
        setup(null, null, null);

        try {
            userValidator.validateUser(user);
        } catch (UniqueFieldsViolationException e) {
            fail("Exception thrown incorrectly");
        }
    }

    @Test
    public void testValidateUserWithExistingEmail() {
        setup(user2, null, true);

        assertThrows(
                UniqueFieldsViolationException.class,
                () -> userValidator.validateUser(user)
        );
    }

    @Test
    public void testValidateUserWithExistingPhone() {
        setup(null, user2, true);

        assertThrows(
                UniqueFieldsViolationException.class,
                () -> userValidator.validateUser(user)
        );
    }

    @Test
    public void testValidateUserWithExistingEmailAndEqualIds() {
        setup(user2, null, false);

        try {
            userValidator.validateUser(user);
        } catch (UniqueFieldsViolationException e) {
            fail("Exception thrown incorrectly");
        }
    }

    @Test
    public void testValidateUserWithExistingPhoneAndEqualIds() {
        setup(null, user2, false);

        try {
            userValidator.validateUser(user);
        } catch (UniqueFieldsViolationException e) {
            fail("Exception thrown incorrectly");
        }
    }
}
