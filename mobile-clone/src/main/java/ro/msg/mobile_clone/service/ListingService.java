package ro.msg.mobile_clone.service;

import ro.msg.mobile_clone.dto.ListingDto;

import java.util.List;

public interface ListingService {

    ListingDto createListing(ListingDto listingDto) throws Exception;

    ListingDto getListingById(Long id);

    List<ListingDto> getAllListings();

    ListingDto updateListing(Long id, ListingDto listingDto) throws Exception;

    void deleteListing(Long id);

    void deleteListingsByUserId(Long userId);
}
