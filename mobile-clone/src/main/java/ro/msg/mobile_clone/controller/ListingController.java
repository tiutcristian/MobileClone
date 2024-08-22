package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.msg.mobile_clone.dto.ListingDto;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.mapper.ListingMapper;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
@AllArgsConstructor
public class ListingController {

    private ListingService listingService;
    private UserService userService;


    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {

        List<Listing> listings = listingService.getAllListings();

        List<ListingDto> listingDTOs = listings.stream()
                .map(ListingMapper.INSTANCE::mapToListingDto)
                .toList();

        return ResponseEntity.ok(listingDTOs);
    }


    @PostMapping("/create")
    public ResponseEntity<ListingDto> addListing(@RequestBody ListingDto listingDto) throws Exception {

        Listing newListing = ListingMapper.INSTANCE.mapToListing(listingDto, userService);
        Listing savedListing = listingService.createListing(newListing);
        ListingDto savedListingDto = ListingMapper.INSTANCE.mapToListingDto(savedListing);

        String currentPath = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString();

        String targetPath = currentPath.replace("/create", "/{id}");

        URI location = ServletUriComponentsBuilder
                .fromUriString(targetPath)
                .buildAndExpand(savedListingDto.id())
                .toUri();

        return ResponseEntity.created(location).body(savedListingDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable Long id) {

        Listing listing = listingService.getListingById(id);
        ListingDto listingDto = ListingMapper.INSTANCE.mapToListingDto(listing);

        return ResponseEntity.ok(listingDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateListing(@PathVariable Long id, @RequestBody ListingDto listingDto) throws Exception {

        Listing listing = ListingMapper.INSTANCE.mapToListing(listingDto, userService);
        Listing updatedListing = listingService.updateListing(id, listing);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .buildAndExpand(updatedListing.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {

        listingService.deleteListing(id);

        return ResponseEntity.ok().build();
    }
}
