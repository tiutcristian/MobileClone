package ro.msg.mobile_clone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.msg.mobile_clone.entity.FuelType;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.entity.Transmission;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.repository.ListingRepository;

import java.time.Year;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {

    @Mock ListingRepository listingRepository;
    @InjectMocks ListingService listingService;

    private User user;
    private Listing listing;
    private Listing listing2;
    private Pageable pageable;

    private void setupPageable() {
        pageable = PageRequest.of(0, 10);
    }

    private void setupUser() {
        user = new User();
        user.setFirstName("Test First Name");
        user.setLastName("Test Last Name");
        user.setEmail("test@mail.com");
        user.setPhone("0712345678");
    }

    @BeforeEach
    void setupListing() {
        setupUser();

        listing = new Listing();
        listing.setUser(user);
        listing.setTitle("Test Title");
        listing.setPrice(100);
        listing.setMake("Test Make");
        listing.setModel("Test Model");
        listing.setDescription("Test Description");
        listing.setYear(Year.of(2020));
        listing.setMileage(10000);
        listing.setEngineSize(2000);
        listing.setHorsepower(150);
        listing.setTransmission(Transmission.AUTOMATIC);
        listing.setFuelType(FuelType.DIESEL);
    }

    private void setupListing2() {
        listing2 = new Listing();
        listing2.setUser(user);
        listing2.setTitle("Test Title 2");
        listing2.setPrice(200);
        listing2.setMake("Test Make 2");
        listing2.setModel("Test Model 2");
        listing2.setDescription("Test Description 2");
        listing2.setYear(Year.of(2021));
        listing2.setMileage(20000);
        listing2.setEngineSize(3000);
        listing2.setHorsepower(250);
        listing2.setTransmission(Transmission.MANUAL);
        listing2.setFuelType(FuelType.PETROL);
    }

    @Test
    void testCreateListing() {
        when(listingRepository.save(listing)).thenReturn(listing);

        Listing result = listingService.createListing(listing);

        assertEquals(listing, result);
    }

    @Test
    void testGetListingById() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        Listing result = null;
        try {
            result = listingService.getListingById(1L);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown when it shouldn't have been.");
        }

        assertEquals(listing, result);
    }

    @Test
    void testGetListingByIdThrowsEntityNotFoundException() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> listingService.getListingById(1L)
        );
    }

    @Test
    void testGetAllPaginated() {
        setupPageable();
        when(listingRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(listing)));

        Page<Listing> result = listingService.getAllPaginated(pageable);

        assertEquals(result.getContent().getFirst(), listing);
    }

    @Test
    void testUpdateListing() {
        setupListing2();
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(listingRepository.save(listing)).thenReturn(listing);

        Listing result = null;
        try {
            result = listingService.updateListing(1L, listing2);
        } catch (EntityNotFoundException e) {
            fail("EntityNotFoundException thrown when it shouldn't have been.");
        }

        assertEquals(listing2, result);
    }

    @Test
    void testUpdateListingThrowsEntityNotFoundException() {
        setupListing2();
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> listingService.updateListing(1L, listing2)
        );
    }

    @Test
    void testDeleteListingValid() {
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        assertDoesNotThrow(() -> listingService.deleteListing(1L));

        verify(listingRepository).delete(listing);
    }

    @Test
    void testDeleteListingInvalid() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> listingService.deleteListing(1L)
        );
    }
}
