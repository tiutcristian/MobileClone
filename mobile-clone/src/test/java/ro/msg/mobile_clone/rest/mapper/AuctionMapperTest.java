package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.AuctionDto;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class AuctionMapperTest {

    @Mock ListingService listingService;
    @Mock UserService userService;

    @Mock Auction auction;
    private AuctionDto auctionDto;
    private final Timestamp endingTimestamp = Timestamp.valueOf("2021-12-31 23:59:59");

    private void setupAuctionDto() {
        auctionDto = new AuctionDto(
                1L,
                2L,
                endingTimestamp,
                3L,
                false
        );
    }

    private void setupAuction() {
        Listing listing = Mockito.mock(Listing.class);
        User winner = Mockito.mock(User.class);

        Mockito.when(auction.getListing()).thenReturn(listing);
        Mockito.when(auction.getWinner()).thenReturn(winner);
        Mockito.when(auction.getEndingTimestamp()).thenReturn(endingTimestamp);
        Mockito.when(auction.isActive()).thenReturn(false);

        Mockito.when(auction.getId()).thenReturn(1L);
        Mockito.when(listing.getId()).thenReturn(2L);
        Mockito.when(winner.getId()).thenReturn(3L);
    }

    @Test
    public void testMapToAuctionDto() {
        setupAuction();

        AuctionDto auctionDto = AuctionMapper.INSTANCE.mapToAuctionDto(auction);

        Assertions.assertEquals(1L, auctionDto.id());
        Assertions.assertEquals(2L, auctionDto.listingId());
        Assertions.assertEquals(endingTimestamp, auctionDto.endingTimestamp());
        Assertions.assertEquals(3L, auctionDto.winnerId());
        Assertions.assertFalse(auctionDto.active());
    }

    @Test
    public void testMapToAuction() {
        setupAuctionDto();
        Listing listing = Mockito.mock(Listing.class);
        User winner = Mockito.mock(User.class);

        Auction result = null;
        try {
            Mockito.when(listingService.getListingById(2L)).thenReturn(listing);
            Mockito.when(userService.getUserById(3L)).thenReturn(winner);
            result = AuctionMapper.INSTANCE.mapToAuction(auctionDto, listingService, userService);
        } catch (EntityNotFoundException e) {
            Assertions.fail("Entity should be found");
        }

        Assertions.assertEquals(listing, result.getListing());
        Assertions.assertEquals(endingTimestamp, result.getEndingTimestamp());
        Assertions.assertEquals(winner, result.getWinner());
        Assertions.assertFalse(result.isActive());
    }
}
