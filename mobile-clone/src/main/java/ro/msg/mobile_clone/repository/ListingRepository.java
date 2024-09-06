package ro.msg.mobile_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.msg.mobile_clone.entity.Listing;

//public interface ListingRepository extends JpaSpecificationExecutor<Listing> {
public interface ListingRepository extends JpaRepository<Listing, Long> {
}
