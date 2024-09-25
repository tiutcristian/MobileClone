package ro.msg.mobile_clone.rest.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.FuelType;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.Transmission;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.service.UserService;

import java.time.Year;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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
            when(userService.getUserById(2L)).thenReturn(user);
        } catch (EntityNotFoundException e) {
            fail("Entity should be found");
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

        assertEquals("title 2", listingDto.title());
        assertEquals(3000, listingDto.price());
        assertEquals("make", listingDto.make());
        assertEquals("model", listingDto.model());
        assertEquals("description", listingDto.description());
        assertEquals(Year.of(2020), listingDto.year());
        assertEquals(1000, listingDto.mileage());
        assertEquals(2000, listingDto.engineSize());
        assertEquals(100, listingDto.horsepower());
        assertEquals(Transmission.MANUAL, listingDto.transmission());
        assertEquals(FuelType.PETROL, listingDto.fuelType());
    }

    @Test
    public void testMapToListing() {
        setupListingDto();

        Listing result = null;
        try {
            result = ListingMapper.INSTANCE.mapToListing(listingDto, userService);
        } catch (EntityNotFoundException e) {
            fail("Entity should be found");
        }

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals("title", result.getTitle());
        assertEquals(2000, result.getPrice());
        assertEquals("Make", result.getMake());
        assertEquals("Model", result.getModel());
        assertEquals("Description", result.getDescription());
        assertEquals(Year.of(2021), result.getYear());
        assertEquals(10000, result.getMileage());
        assertEquals(3000, result.getEngineSize());
        assertEquals(200, result.getHorsepower());
        assertEquals(Transmission.AUTOMATIC, result.getTransmission());
        assertEquals(FuelType.DIESEL, result.getFuelType());
    }
}
