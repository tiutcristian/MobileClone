package ro.msg.mobile_clone.model.validator;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.model.entity.Bid;
import ro.msg.mobile_clone.model.entity.Listing;
import ro.msg.mobile_clone.model.entity.User;
import ro.msg.mobile_clone.other.exceptions.InvalidEntityException;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BidValidator {

    public static void validateBid(Bid b) throws InvalidEntityException {
        Set<String> errors = new HashSet<>();

        Auction a = b.getAuction();
        Listing l = a.getListing();
        User bidder = b.getBidder();
        User seller = l.getUser();

        if (b.getOffer() < l.getPrice()) {
            errors.add("Offer must be at least the listing price");
        }

        if (a.getEndingTimestamp().before(new Timestamp(System.currentTimeMillis()))) {
            errors.add("Auction has ended, cannot bid");
        }

        if (Objects.equals(bidder.getId(), seller.getId())) {
            errors.add("Seller cannot bid on their own auction");
        }

        if (!errors.isEmpty()) {
            throw new InvalidEntityException(Bid.class, errors);
        }
    }
}
