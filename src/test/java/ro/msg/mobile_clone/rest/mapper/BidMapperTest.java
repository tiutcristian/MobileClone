package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.BidDto;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Bid;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.AuctionService;
import ro.msg.mobile_clone.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidMapperTest {

    @Mock AuctionService auctionService;
    @Mock UserService userService;

    @Mock Bid bid;
    private BidDto bidDto;
    double offer = 2500.0;

    private void setupBidDto() {
        bidDto = new BidDto(1L, 2L, 3L, offer);
    }

    private void setupBid() {
        Auction auction = mock(Auction.class);
        User bidder = mock(User.class);

        when(bid.getId()).thenReturn(1L);
        when(bid.getAuction()).thenReturn(auction);
        when(bid.getBidder()).thenReturn(bidder);
        when(bid.getOffer()).thenReturn(offer);

        when(auction.getId()).thenReturn(2L);
        when(bidder.getId()).thenReturn(3L);
    }

    @Test
    public void testMapToBidDto() {
        setupBid();

        BidDto result = BidMapper.INSTANCE.mapToBidDto(bid);

        assertEquals(1L, result.id());
        assertEquals(2L, result.auctionId());
        assertEquals(3L, result.bidderId());
        assertEquals(2500.0, result.offer());
    }

    @Test
    public void testMapToBid() {
        setupBidDto();
        Auction auction = mock(Auction.class);
        User bidder = mock(User.class);

        Bid result = null;
        try {
            when(auctionService.getAuctionById(2L)).thenReturn(auction);
            when(userService.getUserById(3L)).thenReturn(bidder);
            result = BidMapper.INSTANCE.mapToBid(bidDto, auctionService, userService);
        } catch (EntityNotFoundException e) {
            fail("Exception thrown incorrectly");
        }

        assertEquals(auction, result.getAuction());
        assertEquals(bidder, result.getBidder());
        assertEquals(2500.0, result.getOffer());
    }
}
