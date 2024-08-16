package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.service.ListingService;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@AllArgsConstructor
public class ListingController {

    private ListingService listingService;


    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }


    @PostMapping("/create")
    public ResponseEntity<ListingDto> addListing(@RequestBody ListingDto listingDto) {
        return new ResponseEntity<>(listingService.createListing(listingDto), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListing(@PathVariable Long id, @RequestBody ListingDto listingDto) {
        return ResponseEntity.ok(listingService.updateListing(id, listingDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.ok("Listing deleted successfully");
    }
}
