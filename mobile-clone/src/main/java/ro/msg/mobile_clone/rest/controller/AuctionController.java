package ro.msg.mobile_clone.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.other.dto.AuctionDto;
import ro.msg.mobile_clone.other.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.other.mapper.AuctionMapper;
import ro.msg.mobile_clone.service.AuctionService;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
@Slf4j
public class AuctionController {

    private final AuctionService auctionService;
    private final ListingService listingService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<Page<AuctionDto>> getAll(Pageable pageable) {

        Page<AuctionDto> resultPage = auctionService
                .getAllPaginated(pageable)
                .map(AuctionMapper.INSTANCE::mapToAuctionDto);

        log.info("Retrieved page {} of size {} with {} elements",
                resultPage.getNumber(), resultPage.getSize(), resultPage.getNumberOfElements());

        return ResponseEntity.ok(resultPage);
    }


    @PostMapping("/create")
    public ResponseEntity<AuctionDto> addAuction(@RequestBody AuctionDto auctionDto) throws EntityNotFoundException {

        Auction newAuction = AuctionMapper.INSTANCE.mapToAuction(auctionDto, listingService, userService);
        log.debug("DTO mapped to entity: {}", newAuction);

        Auction savedAuction = auctionService.startAuction(newAuction);
        log.debug("Entity saved: {}", savedAuction);

        AuctionDto savedAuctionDto = AuctionMapper.INSTANCE.mapToAuctionDto(savedAuction);
        log.debug("Entity mapped back to DTO: {}", savedAuctionDto);

        log.info("Created auction with id {}", savedAuction.getId());

        return ResponseEntity
                .created(URI.create("/api/v1/auctions/" + savedAuction.getId()))
                .body(savedAuctionDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AuctionDto> getAuctionById(@PathVariable Long id)
            throws EntityNotFoundException {

        Auction auction = auctionService.getAuctionById(id);
        log.debug("Retrieved auction: {}", auction);

        AuctionDto auctionDto = AuctionMapper.INSTANCE.mapToAuctionDto(auction);
        log.debug("Mapped auction to DTO: {}", auctionDto);

        log.info("Retrieved auction with id {}", auction.getId());

        return ResponseEntity.ok(auctionDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id)
            throws EntityNotFoundException {

        auctionService.deleteAuction(id);
        log.info("Deleted auction with id {}", id);

        return ResponseEntity.ok().build();
    }
}
