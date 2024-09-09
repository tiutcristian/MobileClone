package ro.msg.mobile_clone.entity.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.entity.*;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;

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
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(bidder.getId()).thenReturn(2L);
        setUpBid();
    }

    @Test
    void testValidateBidValid() {
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(bidder.getId()).thenReturn(2L);

        try {
            BidValidator.validateBid(bid);
        } catch (InvalidEntityException e) {
            Assertions.fail("Bid should be valid");
        }
    }

    @Test
    void testValidateBidOfferLessThanPrice() {
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(bidder.getId()).thenReturn(2L);
        bid.setOffer(999.0);

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> BidValidator.validateBid(bid)
        );
    }

    @Test
    void testValidateBidAuctionEnded() {
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(bidder.getId()).thenReturn(2L);
        auction.setEndingTimestamp(
                Timestamp.from(
                        Instant.now().minus(1, ChronoUnit.MINUTES)
                )
        );

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> BidValidator.validateBid(bid)
        );
    }

    @Test
    void testValidateBidSellerBid() {
        Mockito.when(bidder.getId()).thenReturn(1L);

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> BidValidator.validateBid(bid)
        );
    }
}
