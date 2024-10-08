package ro.msg.mobile_clone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.msg.mobile_clone.entity.*;
import ro.msg.mobile_clone.entity.validator.BidValidator;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;
import ro.msg.mobile_clone.repository.AuctionRepository;
import ro.msg.mobile_clone.repository.BidRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceTest {

    @Mock BidRepository bidRepository;
    @Mock AuctionRepository auctionRepository;
    @InjectMocks BidService bidService;

    private User user;
    private Listing listing;
    private Auction auction;
    private Bid bid;
    private User bidder;
    private Pageable pageable;

    private void setUpUser() {
        user = new User();
        user.setFirstName("Test First Name");
        user.setLastName("Test Last Name");
        user.setEmail("mail@test.com");
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
        auction.setEndingTimestamp(Timestamp.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
    }

    private void setUpBidder() {
        bidder = new User();
        bidder.setFirstName("Bidder First Name");
        bidder.setLastName("Bidder Last Name");
        bidder.setEmail("bidder@mail.com");
        bidder.setPhone("0712345678");
    }

    @BeforeEach
    void setUpBid() {
        setUpAuction();
        setUpBidder();

        bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(bidder);
        bid.setOffer(1001.0);
    }

    private void setUpPageable() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testPlaceBid() {
        when(bidRepository.save(bid)).thenReturn(bid);

        Bid result = null;
        try (MockedStatic<BidValidator> mockingStatic = mockStatic(BidValidator.class)) {
            mockingStatic.when(() -> BidValidator.validateBid(any()))
                    .then(i -> true);

            try {
                result = bidService.placeBid(bid);
            } catch (InvalidEntityException e) {
                fail("InvalidEntityException thrown when it shouldn't have been.");
            }
        }

        assertEquals(result, bid);
    }

    @Test
    void testPlaceBidInvalid() {
        bid.setOffer(-200.0);

        try (MockedStatic<BidValidator> mockingStatic = mockStatic(BidValidator.class)) {
            mockingStatic.when(() -> BidValidator.validateBid(any()))
                    .thenThrow(new InvalidEntityException(
                            Bid.class,
                            Set.of("Offer must be at least the listing price")
                    ));

            assertThrows(
                    InvalidEntityException.class,
                    () -> bidService.placeBid(bid)
            );
        }
    }

    @Test
    void testGetBidById() {
        when(bidRepository.findById(1L)).thenReturn(java.util.Optional.of(bid));

        Bid result = null;
        try {
            result = bidService.getBidById(1L);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown incorrectly.");
        }

        assertEquals(result, bid);
    }

    @Test
    void testGetBidByIdNotFound() {
        when(bidRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> bidService.getBidById(1L)
        );
    }

    @Test
    void testGetPaginatedBidsByAuction() {
        setUpPageable();
        when(auctionRepository.findById(1L)).thenReturn(java.util.Optional.of(auction));
        when(bidRepository.findBidsByAuction(pageable, auction))
                .thenReturn(new PageImpl<>(Collections.singletonList(bid)));

        Page<Bid> result = null;
        try {
            result = bidService.getPaginatedBidsByAuction(1L, pageable);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown incorrectly.");
        }

        assertEquals(result.getContent().getFirst(), bid);
    }

    @Test
    void testGetPaginatedBidsByAuctionNotFound() {
        setUpPageable();
        when(auctionRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> bidService.getPaginatedBidsByAuction(1L, pageable)
        );
    }

    @Test
    void testDeleteBid() {
        when(bidRepository.findById(1L)).thenReturn(java.util.Optional.of(bid));

        assertDoesNotThrow(() -> bidService.deleteBid(1L));

        verify(bidRepository).delete(bid);
    }

    @Test
    void testDeleteBidNotFound() {
        when(bidRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> bidService.deleteBid(1L)
        );
    }
}
