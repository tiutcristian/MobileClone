package ro.msg.mobile_clone.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.model.entity.Bid;
import ro.msg.mobile_clone.model.entity.User;
import ro.msg.mobile_clone.model.validator.AuctionValidator;
import ro.msg.mobile_clone.other.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.other.exceptions.InvalidAuctionException;
import ro.msg.mobile_clone.repository.AuctionRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    private EntityManager entityManager;


    public Auction startAuction(Auction a) throws InvalidAuctionException {
        AuctionValidator.validateCreatedAuction(a);
        log.debug("Auction validated: {}", a);

        log.debug("Saving auction: {}", a);
        return auctionRepository.save(a);
    }


    public Auction getAuctionById(Long id) throws EntityNotFoundException {
        log.debug("Retrieving auction with id: {}...", id);
        return auctionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Auction.class, id));
    }


    public Page<Auction> getAllPaginated(Pageable pageable) {
        return auctionRepository.findAll(pageable);
    }


    public void deleteAuction(Long id)
            throws EntityNotFoundException {

        log.debug("Retrieving auction with id: {}...", id);
        Auction auction = auctionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Auction.class, id));
        log.debug("Auction retrieved: {}", auction);

        log.debug("Deleting auction...");
        auctionRepository.delete(auction);
    }


    @Scheduled(fixedRate = 60000, initialDelay = 3000) // 1 minute rate | 3 seconds initial delay
    @Transactional
    public void checkFinishedAuctions() {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Auction> cq = cb.createQuery(Auction.class);
        Root<Auction> root = cq.from(Auction.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.lessThanOrEqualTo(root.get("endingTimestamp"), new Timestamp(System.currentTimeMillis())));
        log.debug("Ending timestamp predicate added");
        predicates.add(cb.isNull(root.get("winner")));
        log.debug("Winner predicate added");
        predicates.add(cb.isNotEmpty(root.get("bids")));
        log.debug("Bids predicate added");
        cq.where(predicates.toArray(new Predicate[0]));
        log.debug("Criteria query created");

        List<Auction> resultList = entityManager.createQuery(cq).getResultList();
        log.debug("Got the result list");

        for (Auction a : resultList) {
            log.debug("Auction to be closed: {}", a);
            Optional<Bid> winningBid = a.getBids()
                    .stream()
                    .max((b1, b2) -> (int) (b1.getOffer() - b2.getOffer()));
            if (winningBid.isPresent()) {
                User winner = winningBid.get().getBidder();
                a.setWinner(winner);
                log.debug("Winner set: {}", a.getWinner());
            } else {
                log.error("No winner found. Criteria is not working properly " +
                        "(it should not allow retrieving auctions without bids).");
            }
            auctionRepository.save(a);
            log.debug("Auction saved: {}", a);
        }
    }
}
