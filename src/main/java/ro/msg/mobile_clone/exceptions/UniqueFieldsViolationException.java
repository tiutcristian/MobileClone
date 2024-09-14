package ro.msg.mobile_clone.exceptions;

import java.util.Set;

public class UniqueFieldsViolationException extends Exception {
    public UniqueFieldsViolationException(Class<?> cls, Set<String> fields) {
        super(
                "The following fields of the " +
                cls.getSimpleName() +
                " are already taken: " +
                String.join(", ", fields)
        );
    }
}
