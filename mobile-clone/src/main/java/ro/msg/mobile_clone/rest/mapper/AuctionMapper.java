package ro.msg.mobile_clone.rest.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.dto.AuctionDto;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

@Mapper(
    componentModel = "spring",
    uses = {ListingService.class, UserService.class}
)
public interface AuctionMapper {

    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);

    @Mapping(source = "listing", target = "listingId", qualifiedByName = "listingToListingId")
    @Mapping(source = "winner", target = "winnerId", qualifiedByName = "userToUserId")
    AuctionDto mapToAuctionDto(Auction auction);

    @Mapping(source = "listingId", target = "listing", qualifiedByName = "listingIdToListing")
    @Mapping(source = "winnerId", target = "winner", qualifiedByName = "winnerIdToUser")
    Auction mapToAuction(AuctionDto auctionDto, @Context ListingService listingService, @Context UserService userService)
            throws EntityNotFoundException;

    @Named("listingToListingId")
    default Long listingToListingId(Listing listing) {
        return listing.getId();
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    @Named("listingIdToListing")
    default Listing listingIdToListing(Long listingId, @Context ListingService listingService) throws EntityNotFoundException {
        return listingService.getListingById(listingId);
    }

    @Named("winnerIdToUser")
    default User winnerIdToUser(Long winnerId, @Context UserService userService) throws EntityNotFoundException {
        if (winnerId == null) {
            return null;
        }
        return userService.getUserById(winnerId);
    }
}
