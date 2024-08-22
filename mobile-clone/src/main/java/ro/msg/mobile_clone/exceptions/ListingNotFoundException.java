package ro.msg.mobile_clone.exceptions;

public class ListingNotFoundException extends Exception {
    public ListingNotFoundException() {
        super("Listing not found");
    }
}
