package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.BidDto;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Bid;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.AuctionService;
import ro.msg.mobile_clone.service.UserService;

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
        Auction auction = Mockito.mock(Auction.class);
        User bidder = Mockito.mock(User.class);

        Mockito.when(bid.getId()).thenReturn(1L);
        Mockito.when(bid.getAuction()).thenReturn(auction);
        Mockito.when(bid.getBidder()).thenReturn(bidder);
        Mockito.when(bid.getOffer()).thenReturn(offer);

        Mockito.when(auction.getId()).thenReturn(2L);
        Mockito.when(bidder.getId()).thenReturn(3L);
    }

    @Test
    public void testMapToBidDto() {
        setupBid();

        BidDto result = BidMapper.INSTANCE.mapToBidDto(bid);

        Assertions.assertEquals(1L, result.id());
        Assertions.assertEquals(2L, result.auctionId());
        Assertions.assertEquals(3L, result.bidderId());
        Assertions.assertEquals(2500.0, result.offer());
    }

    @Test
    public void testMapToBid() {
        setupBidDto();
        Auction auction = Mockito.mock(Auction.class);
        User bidder = Mockito.mock(User.class);

        Bid result = null;
        try {
            Mockito.when(auctionService.getAuctionById(2L)).thenReturn(auction);
            Mockito.when(userService.getUserById(3L)).thenReturn(bidder);
            result = BidMapper.INSTANCE.mapToBid(bidDto, auctionService, userService);
        } catch (EntityNotFoundException e) {
            Assertions.fail("Exception thrown incorrectly");
        }

        Assertions.assertEquals(auction, result.getAuction());
        Assertions.assertEquals(bidder, result.getBidder());
        Assertions.assertEquals(2500.0, result.getOffer());
    }
}
