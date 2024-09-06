package ro.msg.mobile_clone.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
public class AuctionServiceTest {
    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionService auctionService;

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

    private void setUpAuction() {
        setUpListing();

        auction = new Auction();
        auction.setListing(listing);
        auction.setEndingTimestamp(Timestamp.from(Instant.now().plus(2, ChronoUnit.MINUTES)));
    }

    private void setUpPageable() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testStartAuctionValid() throws InvalidEntityException {
        // setup
        setUpAuction();
        Mockito.when(auctionRepository.save(auction)).thenReturn(auction);

        // act
        Auction result = auctionService.startAuction(auction);

        // assert
        Assert.assertEquals(result, auction);
    }

    @Test(expected = InvalidEntityException.class)
    public void testStartAuctionInvalid() throws InvalidEntityException {
        // setup
        setUpAuction();
        auction.setWinner(user);

        // act
        auctionService.startAuction(auction);
    }

    @Test(expected = InvalidEntityException.class)
    public void testStartAuctionInvalid2() throws InvalidEntityException {
        // setup
        setUpAuction();
        auction.setEndingTimestamp(Timestamp.from(Instant.now().minus(2, ChronoUnit.MINUTES)));

        // act
        auctionService.startAuction(auction);
    }

    @Test
    public void testGetAuctionByIdValid() throws EntityNotFoundException {
        // setup
        setUpAuction();
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        // act
        Auction result = auctionService.getAuctionById(1L);

        // assert
        Assert.assertEquals(result, auction);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAuctionByIdInvalid() throws EntityNotFoundException {
        // setup
        setUpAuction();
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        // act
        auctionService.getAuctionById(1L);
    }

    @Test
    public void testDeleteAuctionValid() throws EntityNotFoundException {
        // setup
        setUpAuction();
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        // act
        auctionService.deleteAuction(1L);

        // assert
        Mockito.verify(auctionRepository).delete(auction);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteAuctionInvalid() throws EntityNotFoundException {
        // setup
        setUpAuction();
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        // act
        auctionService.deleteAuction(1L);
    }

    @Test
    public void testGetAllPaginated() {
        // setup
        setUpAuction();
        setUpPageable();
        Mockito.when(auctionRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(auction)));

        // act
        Page<Auction> result = auctionService.getAllPaginated(pageable);

        // assert
        Assert.assertEquals(result.getContent().getFirst(), auction);
    }
}
