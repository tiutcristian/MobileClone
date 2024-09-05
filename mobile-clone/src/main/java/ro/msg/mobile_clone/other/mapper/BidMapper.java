package ro.msg.mobile_clone.other.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.model.entity.Bid;
import ro.msg.mobile_clone.model.entity.User;
import ro.msg.mobile_clone.other.dto.BidDto;
import ro.msg.mobile_clone.other.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.AuctionService;
import ro.msg.mobile_clone.service.UserService;

@Mapper(
    componentModel = "spring",
    uses = {AuctionService.class, UserService.class}
)
public interface BidMapper {

    BidMapper INSTANCE = Mappers.getMapper(BidMapper.class);

    @Mapping(source = "auction", target = "auctionId", qualifiedByName = "auctionToAuctionId")
    @Mapping(source = "bidder", target = "bidderId", qualifiedByName = "userToUserId")
    BidDto mapToBidDto(Bid bid);

    @Mapping(source = "auctionId", target = "auction", qualifiedByName = "auctionIdToAuction")
    @Mapping(source = "bidderId", target = "bidder", qualifiedByName = "bidderIdToUser")
    Bid mapToBid(BidDto bidDto, @Context AuctionService auctionService, @Context UserService userService)
            throws EntityNotFoundException;

    @Named("auctionToAuctionId")
    default Long auctionToAuctionId(Auction auction) {
        return auction.getId();
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

    @Named("auctionIdToAuction")
    default Auction auctionIdToAuction(Long auctionId, @Context AuctionService auctionService)
            throws EntityNotFoundException {
        return auctionService.getAuctionById(auctionId);
    }

    @Named("bidderIdToUser")
    default User bidderIdToUser(Long bidderId, @Context UserService userService)
            throws EntityNotFoundException {
        return userService.getUserById(bidderId);
    }
}
