package ro.msg.mobile_clone.other.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Class<?> cls, Long id) {
        super("Entity of type " + cls.getSimpleName() + " with id " + id + " not found");
    }
}
