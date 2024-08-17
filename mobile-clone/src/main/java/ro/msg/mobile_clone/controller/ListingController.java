package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.mapper.ListingMapper;
import ro.msg.mobile_clone.service.ListingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
@AllArgsConstructor
public class ListingController {

    private ListingService listingService;
    private ListingMapper listingMapper;


    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {
        List<Listing> listings = listingService.getAllListings();
        List<ListingDto> listingDTOs = listings.stream()
                .map(ListingMapper::mapToListingDto)
                .toList();
        return ResponseEntity.ok(listingDTOs);
    }


    @PostMapping("/create")
    public ResponseEntity<ListingDto> addListing(@RequestBody ListingDto listingDto) throws Exception {
        Listing newListing = listingMapper.mapToListing(listingDto);
        Listing savedListing = listingService.createListing(newListing);
        ListingDto savedListingDto = ListingMapper.mapToListingDto(savedListing);
        return new ResponseEntity<>(savedListingDto, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable Long id) {
        Listing listing = listingService.getListingById(id);
        ListingDto listingDto = ListingMapper.mapToListingDto(listing);
        return ResponseEntity.ok(listingDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListing(@PathVariable Long id, @RequestBody ListingDto listingDto) throws Exception {
        Listing listing = listingMapper.mapToListing(listingDto);
        listingService.updateListing(id, listing);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.ok().build();
    }
}
