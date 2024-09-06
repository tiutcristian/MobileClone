package ro.msg.mobile_clone.rest.exceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.msg.mobile_clone.other.exceptions.InvalidAuctionException;
import ro.msg.mobile_clone.other.exceptions.UniqueFieldsViolationException;


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


    @ExceptionHandler(InvalidAuctionException.class)
    public ResponseEntity<String> handleInvalidAuctionException(InvalidAuctionException e) {
        String[] errors = e.getConstraintViolations().toArray(String[]::new);

        log.error("Invalid auction error: {}", String.join(" | ", errors));

        return new ResponseEntity<>(
                "Invalid auction error:\n\t" + String.join("\n\t", errors),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
