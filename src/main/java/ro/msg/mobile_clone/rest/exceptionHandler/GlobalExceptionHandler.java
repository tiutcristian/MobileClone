package ro.msg.mobile_clone.rest.exceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {

        String[] errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toArray(String[]::new);

        log.error("Constraint violation errors: {}", String.join(" | ", errors));

        return new ResponseEntity<>(
                "Constraint violation errors:\n\t" + String.join("\n\t", errors),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(UniqueFieldsViolationException.class)
    public ResponseEntity<String> handleUniqueFieldsViolationException(UniqueFieldsViolationException e) {
        log.error("Unique fields violation error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<String> handleInvalidAuctionException(InvalidEntityException e) {
        String[] errors = e.getConstraintViolations().toArray(String[]::new);

        log.error("Invalid {} error: {}", e.getClazz().getSimpleName(), String.join(" | ", errors));

        return new ResponseEntity<>(
                "Invalid " +
                        e.getClazz().getSimpleName() +
                        " error:\n\t" +
                        String.join("\n\t", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
