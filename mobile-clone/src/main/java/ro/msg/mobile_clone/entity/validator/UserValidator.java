package ro.msg.mobile_clone.entity.validator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.repository.UserRepository;

import java.util.HashSet;
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

        if (userRepository.findByEmail(user.getEmail()) != null) {
            problematicFields.add("email");
        }
        if (userRepository.findByPhone(user.getPhone()) != null) {
            problematicFields.add("phone");
        }

        if (!problematicFields.isEmpty()) {
            throw new UniqueFieldsViolationException(User.class, problematicFields);
        }
    }
}
