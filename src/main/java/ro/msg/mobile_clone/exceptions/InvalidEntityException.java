package ro.msg.mobile_clone.exceptions;

import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidEntityException extends Exception {

    Class<?> clazz;
    Set<String> constraintViolations;

    public InvalidEntityException(Class<?> clazz, Set<String> errors) {
        super("Invalid " + clazz.getSimpleName() + ": " + String.join(" | ", errors));
        this.clazz = clazz;
        constraintViolations = Set.copyOf(errors);
    }
}
