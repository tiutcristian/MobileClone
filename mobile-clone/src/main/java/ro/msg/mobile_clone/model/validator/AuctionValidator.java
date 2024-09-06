package ro.msg.mobile_clone.model.validator;

import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.other.exceptions.InvalidEntityException;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class AuctionValidator {

    public static final long MIN_AUCTION_DURATION = 60000; // 1 minute

    public static void validateCreatedAuction(Auction a) throws InvalidEntityException {
        Set<String> errors = new HashSet<>();

        if (a.getWinner() != null) {
            errors.add("Auction cannot have a winner when created");
        }

        if (a.getEndingTimestamp().before(new Timestamp(System.currentTimeMillis() + MIN_AUCTION_DURATION))) {
            errors.add(
                    "Ending timestamp must be at least " +
                    MIN_AUCTION_DURATION +
                    " milliseconds in the future"
            );
        }

        if (!errors.isEmpty()) {
            throw new InvalidEntityException(Auction.class, errors);
        }
    }
}
