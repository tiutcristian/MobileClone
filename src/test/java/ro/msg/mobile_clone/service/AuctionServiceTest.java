package ro.msg.mobile_clone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(auctionRepository.save(auction)).thenReturn(auction);

        Auction result = null;
        try {
            result = auctionService.startAuction(auction);
        } catch (InvalidEntityException e) {
            fail("InvalidEntityException thrown when it shouldn't have been.");
        }

        assertEquals(result, auction);
    }

    @Test
    void testStartAuctionInvalid() {
        auction.setWinner(user);

        assertThrows(
                InvalidEntityException.class,
                () -> auctionService.startAuction(auction)
        );
    }

    @Test
    void testStartAuctionInvalid2() {
        auction.setEndingTimestamp(Timestamp.from(Instant.now().minus(2, ChronoUnit.MINUTES)));

        assertThrows(
                InvalidEntityException.class,
                () -> auctionService.startAuction(auction)
        );
    }

    @Test
    void testGetAuctionByIdValid() {
        when(auctionRepository.findById(1L))
                .thenReturn(Optional.of(auction));

        Auction result = null;
        try {
            result = auctionService.getAuctionById(1L);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown incorrectly.");
        }

        assertEquals(result, auction);
    }

    @Test
    void testGetAuctionByIdInvalid() {
        when(auctionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> auctionService.getAuctionById(1L)
        );
    }

    @Test
    void testDeleteAuctionValid() {
        when(auctionRepository.findById(1L))
                .thenReturn(Optional.of(auction));

        assertDoesNotThrow(() -> auctionService.deleteAuction(1L));

        verify(auctionRepository).delete(auction);
    }

    @Test
    void testDeleteAuctionInvalid() {
        when(auctionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> auctionService.deleteAuction(1L)
        );
    }

    @Test
    void testGetAllPaginated() {
        setUpPageable();
        when(auctionRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(auction)));

        Page<Auction> result = auctionService.getAllPaginated(pageable);

        assertEquals(result.getContent().getFirst(), auction);
    }
}
