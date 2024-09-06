package ro.msg.mobile_clone.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Class<?> clazz, Long id) {
        super("Entity of type " + clazz.getSimpleName() + " with id " + id + " not found");
    }
}
