package ro.msg.mobile_clone.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.repository.UserRepository;

@Component
@AllArgsConstructor
public class ListingMapper {

    private UserRepository userRepository;


    public Listing mapToListing(ListingDto listingDto) {
        User foundUser = userRepository.findById(listingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return Listing.builder()
                .id(listingDto.getId())
                .user(foundUser)
                .title(listingDto.getTitle())
                .price(listingDto.getPrice())
                .make(listingDto.getMake())
                .model(listingDto.getModel())
                .description(listingDto.getDescription())
                .year(listingDto.getYear())
                .mileage(listingDto.getMileage())
                .engineSize(listingDto.getEngineSize())
                .horsepower(listingDto.getHorsepower())
                .transmission(listingDto.getTransmission())
                .fuelType(listingDto.getFuelType())
                .build();
    }

    public static ListingDto mapToListingDto(Listing listing) {
        return ListingDto.builder()
                .id(listing.getId())
                .userId(listing.getUser().getId())
                .title(listing.getTitle())
                .price(listing.getPrice())
                .make(listing.getMake())
                .model(listing.getModel())
                .description(listing.getDescription())
                .year(listing.getYear())
                .mileage(listing.getMileage())
                .engineSize(listing.getEngineSize())
                .horsepower(listing.getHorsepower())
                .transmission(listing.getTransmission())
                .fuelType(listing.getFuelType())
                .build();
    }
}
