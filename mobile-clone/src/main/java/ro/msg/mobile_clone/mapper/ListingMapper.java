package ro.msg.mobile_clone.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFound;
import ro.msg.mobile_clone.service.UserService;

@Mapper(componentModel = "spring", uses = {UserService.class})
public interface ListingMapper {

    ListingMapper INSTANCE = Mappers.getMapper(ListingMapper.class);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    ListingDto mapToListingDto(Listing listing);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    Listing mapToListing(ListingDto listingDto, @Context UserService userService) throws EntityNotFound;

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

    @Named("userIdToUser")
    default User userIdToUser(Long userId, @Context UserService userService) throws EntityNotFound {
        return userService.getUserById(userId);
    }
}
