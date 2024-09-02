package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.repository.ListingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingService {

    private final ListingRepository listingRepository;


    public Listing createListing(Listing l) {
        log.debug("Creating listing: {}", l);
        return listingRepository.save(l);
    }


    public Listing getListingById(Long id) throws EntityNotFoundException {

        log.debug("Retrieving listing with id: {}...", id);
        return listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Listing.class, id));
    }


    public Page<Listing> getAllPaginated(Pageable pageable) {
        return listingRepository.findAll(pageable);
    }


    public Listing updateListing(Long id, @NotNull Listing l) throws EntityNotFoundException {

        log.debug("Retrieving listing with id: {}...", id);
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Listing.class, id));
        log.debug("Listing retrieved: {}", listing);

        listing.setUser(l.getUser());
        log.debug("User updated to: {}", listing.getUser());

        listing.setTitle(l.getTitle());
        log.debug("Title updated to: {}", listing.getTitle());

        listing.setPrice(l.getPrice());
        log.debug("Price updated to: {}", listing.getPrice());

        listing.setMake(l.getMake());
        log.debug("Make updated to: {}", listing.getMake());

        listing.setModel(l.getModel());
        log.debug("Model updated to: {}", listing.getModel());

        listing.setDescription(l.getDescription());
        log.debug("Description updated to: {}", listing.getDescription());

        listing.setYear(l.getYear());
        log.debug("Year updated to: {}", listing.getYear());

        listing.setMileage(l.getMileage());
        log.debug("Mileage updated to: {}", listing.getMileage());

        listing.setEngineSize(l.getEngineSize());
        log.debug("Engine size updated to: {}", listing.getEngineSize());

        listing.setHorsepower(l.getHorsepower());
        log.debug("Horsepower updated to: {}", listing.getHorsepower());

        listing.setTransmission(l.getTransmission());
        log.debug("Transmission updated to: {}", listing.getTransmission());

        listing.setFuelType(l.getFuelType());
        log.debug("Fuel type updated to: {}", listing.getFuelType());

        log.info("Saving listing: {}", listing);
        return listingRepository.save(listing);
    }


    public void deleteListing(Long id) throws EntityNotFoundException {

        log.debug("Retrieving listing with id: {}...", id);
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Listing.class, id));
        log.debug("Listing retrieved: {}", listing);

        log.debug("Deleting listing: {}", listing);
        listingRepository.delete(listing);
    }
}
