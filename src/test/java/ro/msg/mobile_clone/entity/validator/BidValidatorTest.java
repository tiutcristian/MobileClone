package ro.msg.mobile_clone.entity.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.entity.*;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BidValidatorTest {

    @Mock private User user;
    @Mock private User bidder;
    private Listing listing;
    private Auction auction;
    private Bid bid;

    private void setUpListing() {
        listing = new Listing();
        listing.setUser(user);
        listing.setTitle("Test Title");
        listing.setPrice(1000.0);
        listing.setMake("Test Make");
        listing.setModel("Test Model");
        listing.setDescription("Test Description");
        listing.setYear(Year.of(2020));
        listing.setMileage(10000);
        listing.setEngineSize(2000);
        listing.setHorsepower(150);
        listing.setTransmission(Transmission.AUTOMATIC);
        listing.setFuelType(FuelType.DIESEL);
    }

    private void setUpAuction() {
        setUpListing();

        auction = new Auction();
        auction.setListing(listing);
        auction.setEndingTimestamp(
                Timestamp.from(
                        Instant.now().plus(2, ChronoUnit.MINUTES)
                )
        );
    }

    private void setUpBid() {
        setUpAuction();

        bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(bidder);
        bid.setOffer(1001.0);
    }

    @BeforeEach
    void setup() {
        when(user.getId()).thenReturn(1L);
        when(bidder.getId()).thenReturn(2L);
        setUpBid();
    }

    @Test
    void testValidateBidValid() {
        when(user.getId()).thenReturn(1L);
        when(bidder.getId()).thenReturn(2L);

        try {
            BidValidator.validateBid(bid);
        } catch (InvalidEntityException e) {
            fail("Bid should be valid");
        }
    }

    @Test
    void testValidateBidOfferLessThanPrice() {
        when(user.getId()).thenReturn(1L);
        when(bidder.getId()).thenReturn(2L);
        bid.setOffer(999.0);

        assertThrows(
                InvalidEntityException.class,
                () -> BidValidator.validateBid(bid)
        );
    }

    @Test
    void testValidateBidAuctionEnded() {
        when(user.getId()).thenReturn(1L);
        when(bidder.getId()).thenReturn(2L);
        auction.setEndingTimestamp(
                Timestamp.from(
                        Instant.now().minus(1, ChronoUnit.MINUTES)
                )
        );

        assertThrows(
                InvalidEntityException.class,
                () -> BidValidator.validateBid(bid)
        );
    }

    @Test
    void testValidateBidSellerBid() {
        when(bidder.getId()).thenReturn(1L);

        assertThrows(
                InvalidEntityException.class,
                () -> BidValidator.validateBid(bid)
        );
    }
}
