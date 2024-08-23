package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.exceptions.EntityNotFound;
import ro.msg.mobile_clone.repository.ListingRepository;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;


    public Listing createListing(Listing l) {
        return listingRepository.save(l);
    }


    public Listing getListingById(Long id) throws EntityNotFound {
        return listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(Listing.class, id));
    }


    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }


    public Listing updateListing(Long id, @NotNull Listing l) throws EntityNotFound {
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(Listing.class, id));

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

        return listingRepository.save(listing);
    }


    public void deleteListing(Long id) throws EntityNotFound {

        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(Listing.class, id));

        listingRepository.delete(listing);
    }
}
