package ro.msg.mobile_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.mobile_clone.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
