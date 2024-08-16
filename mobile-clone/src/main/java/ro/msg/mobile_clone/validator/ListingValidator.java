package ro.msg.mobile_clone.validator;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ro.msg.mobile_clone.entity.Listing;

import java.time.Year;

@AllArgsConstructor
@Component
public class ListingValidator {

//    UserRepository userRepository;


    public void validate(@NotNull Listing listing) throws Exception {

        StringBuilder errors = new StringBuilder();

        // User validation is done by the mapper when transforming DTO to entity
//        validateUser(listing, errors);

        validatePrice(listing, errors);
        validateYear(listing, errors);
        validateMileage(listing, errors);
        validateEngineSize(listing, errors);
        validateHorsepower(listing, errors);

        // Transmission and fuel type validation is done by Spring Boot when transforming JSON to DTO
//        validateTransmission(listing, errors);
//        validateFuelType(listing, errors);

        if (!errors.isEmpty()) {
            throw new Exception(errors.toString());
        }
    }


//    private void validateUser(@NotNull Listing listing, StringBuilder errors) {
//        if (userRepository
//                .findAll()
//                .stream()
//                .noneMatch(u -> u.getId().equals(listing.getUser().getId()))) {
//            errors.append("User does not exist\n");
//        }
//    }


    private void validatePrice(@NotNull Listing listing, StringBuilder errors) {
        if (listing.getPrice() <= 0) {
            errors.append("Price must be positive\n");
        }
    }


    private void validateYear(@NotNull Listing listing, StringBuilder errors) {
        if (listing.getYear().getValue() < 1900 ||
                listing.getYear().getValue() > Year.now().getValue()) {
            errors.append("Year must be between 1900 and current year\n");
        }
    }


    private void validateMileage(@NotNull Listing listing, StringBuilder errors) {
        if (listing.getMileage() < 0) {
            errors.append("Mileage must be positive\n");
        }
    }


    private void validateEngineSize(@NotNull Listing listing, StringBuilder errors) {
        if (listing.getEngineSize() <= 0) {
            errors.append("Engine size must be positive\n");
        }
    }


    private void validateHorsepower(@NotNull Listing listing, StringBuilder errors) {
        if (listing.getHorsepower() <= 0) {
            errors.append("Horsepower must be positive\n");
        }
    }


//    private void validateTransmission(@NotNull Listing listing, StringBuilder errors) {
//        Set<String> validTransmissions = Set.of("AUTOMATIC", "MANUAL");
//        if (!validTransmissions.contains(listing.getTransmission().toString())) {
//            errors.append("Invalid transmission\n");
//        }
//    }
//
//
//    private void validateFuelType(@NotNull Listing listing, StringBuilder errors) {
//        Set<String> validFuelTypes = Set.of("PETROL", "DIESEL", "ELECTRIC", "HYBRID");
//        if (!validFuelTypes.contains(listing.getFuelType().toString())) {
//            errors.append("Invalid fuel type\n");
//        }
//    }
}
