package ro.msg.mobile_clone.other.exceptions;

import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidAuctionException extends Exception {

    Set<String> constraintViolations;

    public InvalidAuctionException(Set<String> errors) {
        super("Invalid auction: " + String.join(" | ", errors));
        constraintViolations = Set.copyOf(errors);
    }
}
