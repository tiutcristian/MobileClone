package ro.msg.mobile_clone.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.Auction;
import ro.msg.mobile_clone.entity.Bid;
import ro.msg.mobile_clone.entity.validator.BidValidator;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.InvalidEntityException;
import ro.msg.mobile_clone.repository.AuctionRepository;
import ro.msg.mobile_clone.repository.BidRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    public Bid placeBid(Bid b) throws InvalidEntityException {
        BidValidator.validateBid(b);
        log.debug("Bid validated: {}", b);

        log.debug("Placing bid: {}", b);
        return bidRepository.save(b);
    }


    public Bid getBidById(Long id) throws EntityNotFoundException {
        log.debug("Retrieving bid with id: {}...", id);
        return bidRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Bid.class, id));
    }


    public Page<Bid> getPaginatedBidsByAuction(Long auctionId, Pageable pageable)
            throws EntityNotFoundException {

        log.debug("Retrieving bids for auction with id: {}...", auctionId);

        Auction a = auctionRepository
                .findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException(Auction.class, auctionId));

        return bidRepository.findBidsByAuction(pageable, a);
    }


    public void deleteBid(Long id)
            throws EntityNotFoundException {

        log.debug("Retrieving bid with id: {}...", id);
        Bid bid = bidRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Bid.class, id));
        log.debug("Bid retrieved: {}", bid);

        log.debug("Deleting bid...");
        bidRepository.delete(bid);
    }
}
