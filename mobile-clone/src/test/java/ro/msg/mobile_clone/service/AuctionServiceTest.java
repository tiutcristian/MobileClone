package ro.msg.mobile_clone.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.model.entity.Listing;
import ro.msg.mobile_clone.model.entity.User;
import ro.msg.mobile_clone.model.utils.FuelType;
import ro.msg.mobile_clone.model.utils.Transmission;
import ro.msg.mobile_clone.other.exceptions.InvalidAuctionException;
import ro.msg.mobile_clone.repository.AuctionRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AuctionServiceTest {
    @Mock
    private AuctionRepository myRepository;

    @InjectMocks
    private AuctionService auctionService;

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

    private void setUpAuction() {
        setUpListing();

        auction = new Auction();
        auction.setListing(listing);
//        auction.setEndingTimestamp(Timestamp.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
    }

    @Test
    public void testStartAuction() throws InvalidAuctionException {
        // setup
        setUpAuction();
        Mockito.when(myRepository.save(auction)).thenReturn(auction);

        // act
        Auction result = auctionService.startAuction(auction);

        // assert
        Assert.assertEquals(result, auction);
    }

    @Test
    public void testProcessData_NotFound() {
        // Arrange: Mock returning an empty Optional
        Long id = 2L;
        Mockito.when(myRepository.findById(id)).thenReturn(Optional.empty());

        // Act: Call the method
//        String result = myService.processData(id);

        // Assert: Verify the result when no data is found
//        Assert.assertEquals("No Data Found", result);
    }
}
