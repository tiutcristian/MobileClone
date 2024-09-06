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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.Listing;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.repository.ListingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ListingService {

    private final ListingRepository listingRepository;

    @Autowired
    private EntityManager entityManager;


    public Listing createListing(Listing l) {
        log.debug("Creating listing: {}", l);
        return listingRepository.save(l);
    }


    public Listing getListingById(Long id) throws EntityNotFoundException {

        log.debug("Retrieving listing with id: {}...", id);
        return listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Listing.class, id));
    }


    public Page<Listing> getAllPaginated(Pageable pageable) {
        return listingRepository.findAll(pageable);
    }


    public Listing updateListing(Long id, @NotNull Listing l) throws EntityNotFoundException {

        log.debug("Retrieving listing with id: {}...", id);
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Listing.class, id));
        log.debug("Listing retrieved: {}", listing);

        listing.setUser(l.getUser());
        log.debug("User updated to: {}", listing.getUser());

        listing.setTitle(l.getTitle());
        log.debug("Title updated to: {}", listing.getTitle());

        listing.setPrice(l.getPrice());
        log.debug("Price updated to: {}", listing.getPrice());

        listing.setMake(l.getMake());
        log.debug("Make updated to: {}", listing.getMake());

        listing.setModel(l.getModel());
        log.debug("Model updated to: {}", listing.getModel());

        listing.setDescription(l.getDescription());
        log.debug("Description updated to: {}", listing.getDescription());

        listing.setYear(l.getYear());
        log.debug("Year updated to: {}", listing.getYear());

        listing.setMileage(l.getMileage());
        log.debug("Mileage updated to: {}", listing.getMileage());

        listing.setEngineSize(l.getEngineSize());
        log.debug("Engine size updated to: {}", listing.getEngineSize());

        listing.setHorsepower(l.getHorsepower());
        log.debug("Horsepower updated to: {}", listing.getHorsepower());

        listing.setTransmission(l.getTransmission());
        log.debug("Transmission updated to: {}", listing.getTransmission());

        listing.setFuelType(l.getFuelType());
        log.debug("Fuel type updated to: {}", listing.getFuelType());

        log.info("Saving listing: {}", listing);
        return listingRepository.save(listing);
    }


    public void deleteListing(Long id) throws EntityNotFoundException {

        log.debug("Retrieving listing with id: {}...", id);
        Listing listing = listingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Listing.class, id));
        log.debug("Listing retrieved: {}", listing);

        log.debug("Deleting listing: {}", listing);
        listingRepository.delete(listing);
    }


    public Page<Listing> searchListings(Map<String, Object> params, Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Listing> cq = cb.createQuery(Listing.class);
        Root<Listing> root = cq.from(Listing.class);

        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String fieldName = entry.getKey();
            if(!fieldName.equals("page") && !fieldName.equals("size")) {
                Object fieldValue = entry.getValue();

                if (fieldValue != null) {
                    Predicate predicate = cb.equal(root.get(fieldName), fieldValue);
                    predicates.add(predicate);
                    log.debug("Added predicate for field {} with value {}", fieldName, fieldValue);
                }
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        log.debug("Criteria query created");

        List<Listing> resultList = entityManager.createQuery(cq)
                .getResultList();
        log.debug("Got the result list");

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());
        log.debug("Start: {}, end: {}", start, end);

        log.debug("Returning corresponding page");
        return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
    }
}
