package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.AuctionDto;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Listing listing = mock(Listing.class);
        User winner = mock(User.class);

        when(auction.getListing()).thenReturn(listing);
        when(auction.getWinner()).thenReturn(winner);
        when(auction.getEndingTimestamp()).thenReturn(endingTimestamp);
        when(auction.isActive()).thenReturn(false);

        when(auction.getId()).thenReturn(1L);
        when(listing.getId()).thenReturn(2L);
        when(winner.getId()).thenReturn(3L);
    }

    @Test
    public void testMapToAuctionDto() {
        setupAuction();

        AuctionDto auctionDto = AuctionMapper.INSTANCE.mapToAuctionDto(auction);

        assertEquals(1L, auctionDto.id());
        assertEquals(2L, auctionDto.listingId());
        assertEquals(endingTimestamp, auctionDto.endingTimestamp());
        assertEquals(3L, auctionDto.winnerId());
        assertFalse(auctionDto.active());
    }

    @Test
    public void testMapToAuction() {
        setupAuctionDto();
        Listing listing = mock(Listing.class);
        User winner = mock(User.class);

        Auction result = null;
        try {
            when(listingService.getListingById(2L)).thenReturn(listing);
            when(userService.getUserById(3L)).thenReturn(winner);
            result = AuctionMapper.INSTANCE.mapToAuction(auctionDto, listingService, userService);
        } catch (EntityNotFoundException e) {
            fail("Entity should be found");
        }

        assertEquals(listing, result.getListing());
        assertEquals(endingTimestamp, result.getEndingTimestamp());
        assertEquals(winner, result.getWinner());
        assertFalse(result.isActive());
    }
}
