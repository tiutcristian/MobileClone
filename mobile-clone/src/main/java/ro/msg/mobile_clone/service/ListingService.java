package ro.msg.mobile_clone.service;

import ro.msg.mobile_clone.dto.ListingDto;

import java.util.List;

public interface ListingService {

    ListingDto createListing(ListingDto listingDto);

    ListingDto getListingById(Long id);

    List<ListingDto> getAllListings();

    ListingDto updateListing(Long id, ListingDto listingDto);

    void deleteListing(Long id);
}
