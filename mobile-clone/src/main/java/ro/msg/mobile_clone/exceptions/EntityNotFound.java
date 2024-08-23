package ro.msg.mobile_clone.exceptions;

public class EntityNotFound extends Exception {
    public EntityNotFound(Class<?> cls, Long id) {
        super("Entity of type " + cls.getName() + " with id " + id + " not found");
    }
}
