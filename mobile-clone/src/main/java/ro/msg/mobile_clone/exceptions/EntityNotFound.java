package ro.msg.mobile_clone.exceptions;

public class EntityNotFound extends Exception {
    public EntityNotFound(Class<?> cls, Long id) {
        super("Entity of type " + cls.getSimpleName() + " with id " + id + " not found");
    }
}
