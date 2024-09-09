package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.FuelType;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.Transmission;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.UserService;

import java.time.Year;

@ExtendWith(MockitoExtension.class)
public class ListingMapperTest {

    @Mock UserService userService;

    @Mock User user;
    private Listing listing;
    private ListingDto listingDto;

    private void setupListingDto() {
        listingDto = new ListingDto(
                1L,
                2L,
                "title",
                2000,
                "Make",
                "Model",
                "Description",
                Year.of(2021),
                10000,
                3000,
                200,
                Transmission.AUTOMATIC,
                FuelType.DIESEL
        );

        try {
            Mockito.when(userService.getUserById(2L)).thenReturn(user);
        } catch (EntityNotFoundException e) {
            Assertions.fail("Entity should be found");
        }
    }

    private void setupListing() {
        listing = new Listing();
        listing.setUser(user);
        listing.setTitle("title 2");
        listing.setPrice(3000);
        listing.setMake("make");
        listing.setModel("model");
        listing.setDescription("description");
        listing.setYear(Year.of(2020));
        listing.setMileage(1000);
        listing.setEngineSize(2000);
        listing.setHorsepower(100);
        listing.setTransmission(Transmission.MANUAL);
        listing.setFuelType(FuelType.PETROL);
    }

    @Test
    public void testMapToListingDto() {
        setupListing();

        ListingDto listingDto = ListingMapper.INSTANCE.mapToListingDto(listing);

        Assertions.assertEquals("title 2", listingDto.title());
        Assertions.assertEquals(3000, listingDto.price());
        Assertions.assertEquals("make", listingDto.make());
        Assertions.assertEquals("model", listingDto.model());
        Assertions.assertEquals("description", listingDto.description());
        Assertions.assertEquals(Year.of(2020), listingDto.year());
        Assertions.assertEquals(1000, listingDto.mileage());
        Assertions.assertEquals(2000, listingDto.engineSize());
        Assertions.assertEquals(100, listingDto.horsepower());
        Assertions.assertEquals(Transmission.MANUAL, listingDto.transmission());
        Assertions.assertEquals(FuelType.PETROL, listingDto.fuelType());
    }

    @Test
    public void testMapToListing() {
        setupListingDto();

        Listing result = null;
        try {
            result = ListingMapper.INSTANCE.mapToListing(listingDto, userService);
        } catch (EntityNotFoundException e) {
            Assertions.fail("Entity should be found");
        }

        Assertions.assertEquals(user, result.getUser());
        Assertions.assertEquals("title", result.getTitle());
        Assertions.assertEquals(2000, result.getPrice());
        Assertions.assertEquals("Make", result.getMake());
        Assertions.assertEquals("Model", result.getModel());
        Assertions.assertEquals("Description", result.getDescription());
        Assertions.assertEquals(Year.of(2021), result.getYear());
        Assertions.assertEquals(10000, result.getMileage());
        Assertions.assertEquals(3000, result.getEngineSize());
        Assertions.assertEquals(200, result.getHorsepower());
        Assertions.assertEquals(Transmission.AUTOMATIC, result.getTransmission());
        Assertions.assertEquals(FuelType.DIESEL, result.getFuelType());
    }
}
