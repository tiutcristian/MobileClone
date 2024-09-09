package ro.msg.mobile_clone.entity.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.entity.*;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class AuctionValidatorTest {

    private User user;
    private Listing listing;
    private Auction auction;

    private void setUpUser() {
        user = new User();
        user.setFirstName("Test First Name");
        user.setLastName("Test Last Name");
        user.setEmail("first_last@gmail.com");
        user.setPhone("0712345678");
    }

    private void setUpListing() {
        setUpUser();

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

    @BeforeEach
    void setUpAuction() {
        setUpListing();

        auction = new Auction();
        auction.setListing(listing);
        auction.setEndingTimestamp(Timestamp.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
    }

    @Test
    void testValidateCreatedAuctionValid() {
        try {
            AuctionValidator.validateCreatedAuction(auction);
        } catch (InvalidEntityException e) {
            Assertions.fail("Auction should be valid");
        }
    }

    @Test
    void testValidateCreatedAuctionWinnerNotNull() {
        auction.setWinner(new User());

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> AuctionValidator.validateCreatedAuction(auction)
        );
    }

    @Test
    void testValidateCreatedAuctionEndingTimestampBeforeNow() {
        auction.setEndingTimestamp(Timestamp.from(Instant.now().minus(1, ChronoUnit.MINUTES)));

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> AuctionValidator.validateCreatedAuction(auction)
        );
    }
}
