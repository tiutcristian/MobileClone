package ro.msg.mobile_clone.rest.exceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations()
                .forEach(constraintViolation -> sb
                        .append(constraintViolation.getMessage())
                        .append("\n")
                );
        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UniqueFieldsViolationException.class)
    public ResponseEntity<String> handleUniqueFieldsViolationException(UniqueFieldsViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
