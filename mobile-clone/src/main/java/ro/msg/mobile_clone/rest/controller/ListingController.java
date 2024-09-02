package ro.msg.mobile_clone.rest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.mapper.ListingMapper;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
@AllArgsConstructor
@Slf4j
public class ListingController {

    private ListingService listingService;
    private UserService userService;


    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {

        List<Listing> listings = listingService.getAllListings();
        log.debug("Found {} listings", listings.size());

        List<ListingDto> listingDTOs = listings.stream()
                .map(ListingMapper.INSTANCE::mapToListingDto)
                .toList();
        log.debug("Converted {} listings to DTOs", listingDTOs.size());

        log.info("Retrieved all listings");

        return ResponseEntity.ok(listingDTOs);
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

        String currentPath = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString();
        log.debug("Current path: {}", currentPath);

        String targetPath = currentPath.replace("/create", "/{id}");
        log.debug("Target path: {}", targetPath);

        URI location = ServletUriComponentsBuilder
                .fromUriString(targetPath)
                .buildAndExpand(savedListingDto.id())
                .toUri();
        log.debug("Location: {}", location);

        return ResponseEntity.created(location).body(savedListingDto);
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
}
