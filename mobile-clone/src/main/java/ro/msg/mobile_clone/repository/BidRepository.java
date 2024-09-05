package ro.msg.mobile_clone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.mobile_clone.model.entity.Auction;
import ro.msg.mobile_clone.model.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Page<Bid> findBidsByAuction(Pageable pageable, Auction auction);
}
