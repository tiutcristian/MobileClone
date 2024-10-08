package ro.msg.mobile_clone.entity.validator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;


    public void validateUser(User user) throws UniqueFieldsViolationException {
        validateUniqueFields(user);
    }


    private void validateUniqueFields(@NotNull User user) throws UniqueFieldsViolationException {

        Set<String> problematicFields = new HashSet<>();

        try {
            User userByEmail = userRepository.findByEmail(user.getEmail());
            if (userByEmail != null && !Objects.equals(userByEmail.getId(), user.getId())) {
                problematicFields.add("email");
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            problematicFields.add("email");
        }

        try {
            User userByPhone = userRepository.findByPhone(user.getPhone());
            if (userByPhone != null && !Objects.equals(userByPhone.getId(), user.getId())) {
                problematicFields.add("phone");
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            problematicFields.add("phone");
        }

        if (!problematicFields.isEmpty()) {
            throw new UniqueFieldsViolationException(User.class, problematicFields);
        }
    }
}
