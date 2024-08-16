package ro.msg.mobile_clone.validator;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.repository.UserRepository;

@AllArgsConstructor
@Component
public class UserValidator {

    UserRepository userRepository;

    public void validate(@NotNull User user) throws Exception {
        StringBuilder errors = new StringBuilder();

        if (user.getFirstName().length() < 2) {
            errors.append("First name must be at least 2 characters long\n");
        }

        if (user.getLastName().length() < 2) {
            errors.append("Last name must be at least 2 characters long\n");
        }

        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            errors.append("Invalid email address\n");
        } else if (userRepository
                .findAll()
                .stream()
                .anyMatch(u -> !u.getId().equals(user.getId()) && u.getEmail().equals(user.getEmail()))) {
            errors.append("Email address already exists\n");
        }

        if (!user.getPhone().matches("^\\+?[0-9]{10,14}$")) {
            errors.append("Invalid phone number\n");
        } else if (userRepository
                .findAll()
                .stream()
                .anyMatch(u -> !u.getId().equals(user.getId()) && u.getPhone().equals(user.getPhone()))) {
            errors.append("Phone number already exists\n");
        }

        if (!errors.isEmpty()) {
            throw new Exception(errors.toString());
        }
    }
}
