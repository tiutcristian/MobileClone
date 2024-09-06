package ro.msg.mobile_clone.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.model.entity.Bid;
import ro.msg.mobile_clone.other.dto.BidDto;
import ro.msg.mobile_clone.other.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.other.exceptions.InvalidEntityException;
import ro.msg.mobile_clone.other.mapper.BidMapper;
import ro.msg.mobile_clone.service.AuctionService;
import ro.msg.mobile_clone.service.BidService;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {

    private final BidService bidService;
    private final AuctionService auctionService;
    private final UserService userService;


    @PostMapping("/place")
    public ResponseEntity<BidDto> placeBid(@RequestBody BidDto bidDto)
            throws EntityNotFoundException, InvalidEntityException {

        Bid newBid = BidMapper.INSTANCE.mapToBid(bidDto, auctionService, userService);
        log.debug("DTO mapped to entity: {}", newBid);

        Bid savedBid = bidService.placeBid(newBid);
        log.debug("Entity saved: {}", savedBid);

        BidDto savedBidDto = BidMapper.INSTANCE.mapToBidDto(savedBid);
        log.debug("Entity mapped back to DTO: {}", savedBidDto);

        log.info("Placed bid with id {}", savedBid.getId());

        return ResponseEntity
                .created(URI.create("/api/v1/bids/" + savedBid.getId()))
                .body(savedBidDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BidDto> getBidById(@PathVariable Long id) throws EntityNotFoundException {

        Bid bid = bidService.getBidById(id);
        log.debug("Retrieved bid: {}", bid);

        BidDto bidDto = BidMapper.INSTANCE.mapToBidDto(bid);
        log.debug("Entity mapped to DTO: {}", bidDto);

        log.info("Retrieved bid with id {}", bid.getId());

        return ResponseEntity.ok(bidDto);
    }


    @GetMapping
    public ResponseEntity<Page<BidDto>> getPaginatedBidsByAuction(@RequestParam Long auctionId, Pageable pageable)
            throws EntityNotFoundException {

        Page<BidDto> resultPage = bidService
                .getPaginatedBidsByAuction(auctionId, pageable)
                .map(BidMapper.INSTANCE::mapToBidDto);

        log.info("Retrieved page {} of size {} with {} elements",
                resultPage.getNumber(), resultPage.getSize(), resultPage.getNumberOfElements());

        return ResponseEntity.ok(resultPage);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long id)
            throws EntityNotFoundException {

        bidService.deleteBid(id);
        log.info("Deleted bid with id {}", id);

        return ResponseEntity.ok().build();
    }
}
