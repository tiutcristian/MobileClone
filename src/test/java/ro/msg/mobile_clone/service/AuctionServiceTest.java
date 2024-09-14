package ro.msg.mobile_clone.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.entity.FuelType;
import ro.msg.mobile_clone.entity.Transmission;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;
import ro.msg.mobile_clone.repository.AuctionRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock AuctionRepository auctionRepository;
    @InjectMocks AuctionService auctionService;

    private User user;
    private Listing listing;
    private Auction auction;
    private Pageable pageable;

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

    private void setUpPageable() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testStartAuctionValid() {
        Mockito.when(auctionRepository.save(auction)).thenReturn(auction);

        Auction result = null;
        try {
            result = auctionService.startAuction(auction);
        } catch (InvalidEntityException e) {
            Assertions.fail("InvalidEntityException thrown when it shouldn't have been.");
        }

        Assertions.assertEquals(result, auction);
    }

    @Test
    void testStartAuctionInvalid() {
        auction.setWinner(user);

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> auctionService.startAuction(auction)
        );
    }

    @Test
    void testStartAuctionInvalid2() {
        auction.setEndingTimestamp(Timestamp.from(Instant.now().minus(2, ChronoUnit.MINUTES)));

        Assertions.assertThrows(
                InvalidEntityException.class,
                () -> auctionService.startAuction(auction)
        );
    }

    @Test
    void testGetAuctionByIdValid() {
        Mockito.when(auctionRepository.findById(1L))
                .thenReturn(Optional.of(auction));

        Auction result = null;
        try {
            result = auctionService.getAuctionById(1L);
        } catch (EntityNotFoundException e) {
            Assertions.fail("EntityNotFoundException thrown incorrectly.");
        }

        Assertions.assertEquals(result, auction);
    }

    @Test
    void testGetAuctionByIdInvalid() {
        Mockito.when(auctionRepository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> auctionService.getAuctionById(1L)
        );
    }

    @Test
    void testDeleteAuctionValid() {
        Mockito.when(auctionRepository.findById(1L))
                .thenReturn(Optional.of(auction));

        Assertions.assertDoesNotThrow(() -> auctionService.deleteAuction(1L));

        Mockito.verify(auctionRepository).delete(auction);
    }

    @Test
    void testDeleteAuctionInvalid() {
        Mockito.when(auctionRepository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> auctionService.deleteAuction(1L)
        );
    }

    @Test
    void testGetAllPaginated() {
        setUpPageable();
        Mockito.when(auctionRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(auction)));

        Page<Auction> result = auctionService.getAllPaginated(pageable);

        Assertions.assertEquals(result.getContent().getFirst(), auction);
    }
}
