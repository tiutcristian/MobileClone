package ro.msg.mobile_clone.mapper;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.repository.UserRepository;

@Component
@AllArgsConstructor
public class ListingMapper {

    private UserRepository userRepository;


    public Listing mapToListing(@NotNull ListingDto listingDto) {
        User foundUser = userRepository.findById(listingDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Listing listing = new Listing();
        listing.setUser(foundUser);
        listing.setTitle(listingDto.title());
        listing.setPrice(listingDto.price());
        listing.setMake(listingDto.make());
        listing.setModel(listingDto.model());
        listing.setDescription(listingDto.description());
        listing.setYear(listingDto.year());
        listing.setMileage(listingDto.mileage());
        listing.setEngineSize(listingDto.engineSize());
        listing.setHorsepower(listingDto.horsepower());
        listing.setTransmission(listingDto.transmission());
        listing.setFuelType(listingDto.fuelType());
        return listing;
    }

    @Contract("_ -> new")
    public static @NotNull ListingDto mapToListingDto(@NotNull Listing listing) {
        return new ListingDto(
                listing.getId(),
                listing.getUser().getId(),
                listing.getTitle(),
                listing.getPrice(),
                listing.getMake(),
                listing.getModel(),
                listing.getDescription(),
                listing.getYear(),
                listing.getMileage(),
                listing.getEngineSize(),
                listing.getHorsepower(),
                listing.getTransmission(),
                listing.getFuelType()
        );
    }
}
