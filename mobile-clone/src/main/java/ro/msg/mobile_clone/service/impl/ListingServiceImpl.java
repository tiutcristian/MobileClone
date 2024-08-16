package ro.msg.mobile_clone.service.impl;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.mapper.ListingMapper;
import ro.msg.mobile_clone.repository.ListingRepository;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.validator.ListingValidator;

import java.util.List;

@Service
@AllArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final ListingValidator listingValidator;


    @Override
    public ListingDto createListing(ListingDto listingDto) throws Exception {

        Listing listing = listingMapper.mapToListing(listingDto);

        listingValidator.validate(listing);

        Listing savedListing = listingRepository.save(listing);
        return ListingMapper.mapToListingDto(savedListing);
    }


    @Override
    public ListingDto getListingById(Long id) {

        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        return ListingMapper.mapToListingDto(listing);
    }


    @Override
    public List<ListingDto> getAllListings() {

        List<Listing> listings = listingRepository.findAll();

        return listings.stream()
                .map(ListingMapper::mapToListingDto)
                .toList();
    }


    @Override
    public ListingDto updateListing(Long id, @NotNull ListingDto listingDto) throws Exception {
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (listingDto.getTitle() != null) {
            listing.setTitle(listingDto.getTitle());
        }
        if (listingDto.getPrice() != 0) {
            listing.setPrice(listingDto.getPrice());
        }
        if (listingDto.getMake() != null) {
            listing.setMake(listingDto.getMake());
        }
        if (listingDto.getModel() != null) {
            listing.setModel(listingDto.getModel());
        }
        if (listingDto.getDescription() != null) {
            listing.setDescription(listingDto.getDescription());
        }
        if (listingDto.getYear() != null) {
            listing.setYear(listingDto.getYear());
        }
        if (listingDto.getMileage() != 0) {
            listing.setMileage(listingDto.getMileage());
        }
        if (listingDto.getEngineSize() != 0) {
            listing.setEngineSize(listingDto.getEngineSize());
        }
        if (listingDto.getHorsepower() != 0) {
            listing.setHorsepower(listingDto.getHorsepower());
        }
        if (listingDto.getTransmission() != null) {
            listing.setTransmission(listingDto.getTransmission());
        }
        if (listingDto.getFuelType() != null) {
            listing.setFuelType(listingDto.getFuelType());
        }

        listingValidator.validate(listing);

        Listing updatedListing = listingRepository.save(listing);
        return ListingMapper.mapToListingDto(updatedListing);
    }


    @Override
    public void deleteListing(Long id) {

        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listingRepository.delete(listing);
    }


    @Override
    public void deleteListingsByUserId(Long userId) {

        List<Listing> listings = listingRepository.findAll().stream()
                .filter(listing -> listing.getUser().getId().equals(userId))
                .toList();

        listingRepository.deleteAll(listings);
    }
}
