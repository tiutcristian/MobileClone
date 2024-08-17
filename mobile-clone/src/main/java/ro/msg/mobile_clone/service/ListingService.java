package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.repository.ListingRepository;
import ro.msg.mobile_clone.validator.ListingValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final ListingValidator listingValidator;


    public Listing createListing(Listing l) throws Exception {
        listingValidator.validate(l);
        return listingRepository.save(l);
    }


    public Listing getListingById(Long id) {
        return listingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }


    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }


    public void updateListing(Long id, @NotNull Listing l) throws Exception {
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listing.setUser(l.getUser());
        listing.setTitle(l.getTitle());
        listing.setPrice(l.getPrice());
        listing.setMake(l.getMake());
        listing.setModel(l.getModel());
        listing.setDescription(l.getDescription());
        listing.setYear(l.getYear());
        listing.setMileage(l.getMileage());
        listing.setEngineSize(l.getEngineSize());
        listing.setHorsepower(l.getHorsepower());
        listing.setTransmission(l.getTransmission());
        listing.setFuelType(l.getFuelType());

        listingValidator.validate(listing);

        listingRepository.save(listing);
    }


    public void deleteListing(Long id) {

        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listingRepository.delete(listing);
    }


    public void deleteListingsByUserId(Long userId) {

        List<Listing> listings = listingRepository.findAll().stream()
                .filter(listing -> listing.getUser().getId().equals(userId))
                .toList();

        listingRepository.deleteAll(listings);
    }
}
