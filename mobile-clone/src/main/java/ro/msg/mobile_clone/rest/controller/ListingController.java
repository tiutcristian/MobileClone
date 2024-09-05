package ro.msg.mobile_clone.rest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.msg.mobile_clone.other.dto.ListingDto;
import ro.msg.mobile_clone.model.entity.Listing;
import ro.msg.mobile_clone.other.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.other.mapper.ListingMapper;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/listings")
@AllArgsConstructor
@Slf4j
public class ListingController {

    private ListingService listingService;
    private UserService userService;


    @GetMapping
    public ResponseEntity<Page<ListingDto>> getAll(Pageable pageable) {

        Page<ListingDto> resultPage = listingService
                .getAllPaginated(pageable)
                .map(ListingMapper.INSTANCE::mapToListingDto);

        log.info("Retrieved page {} of size {} with {} elements",
                resultPage.getNumber(), resultPage.getSize(), resultPage.getNumberOfElements());

        return ResponseEntity.ok(resultPage);
    }


    @PostMapping("/create")
    public ResponseEntity<ListingDto> addListing(@RequestBody ListingDto listingDto)
            throws EntityNotFoundException {

        Listing newListing = ListingMapper.INSTANCE.mapToListing(listingDto, userService);
        log.debug("Mapped ListingDto to Listing");

        Listing savedListing = listingService.createListing(newListing);
        log.debug("Saved Listing with id {}", savedListing.getId());

        ListingDto savedListingDto = ListingMapper.INSTANCE.mapToListingDto(savedListing);
        log.debug("Mapped Listing with id {} to ListingDto", savedListing.getId());

        log.info("Created listing with id {}", savedListing.getId());

        return ResponseEntity
                .created(URI.create("/api/v1/listings/" + savedListing.getId()))
                .body(savedListingDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable Long id)
            throws EntityNotFoundException {

        Listing listing = listingService.getListingById(id);
        log.debug("Found listing with id {}", listing.getId());

        ListingDto listingDto = ListingMapper.INSTANCE.mapToListingDto(listing);
        log.debug("Converted listing with id {} to ListingDto", listing.getId());

        log.info("Retrieved listing with id {}", listing.getId());

        return ResponseEntity.ok(listingDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateListing(@PathVariable Long id, @RequestBody ListingDto listingDto)
            throws EntityNotFoundException {

        Listing listing = ListingMapper.INSTANCE.mapToListing(listingDto, userService);
        log.debug("DTO mapped to entity: {}", listing);

        Listing updatedListing = listingService.updateListing(id, listing);
        log.debug("Updated entity: {}", updatedListing);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .buildAndExpand(updatedListing.getId())
                .toUri();
        log.debug("Location: {}", location);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        log.debug("Headers: {}", headers);

        log.info("Listing with id {} updated successfully", id);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id)
            throws EntityNotFoundException {

        listingService.deleteListing(id);
        log.info("Listing with id {} deleted successfully", id);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ListingDto>> searchListings(
            @RequestParam Map<String, Object> allParams,
            @PageableDefault Pageable pageable
    ) {

        Page<ListingDto> resultPage = listingService
                .searchListings(allParams, pageable)
                .map(ListingMapper.INSTANCE::mapToListingDto);

        log.info("Retrieved page {} of size {} with {} elements",
                resultPage.getNumber(), resultPage.getSize(), resultPage.getNumberOfElements());

        return ResponseEntity.ok(resultPage);
    }


}
