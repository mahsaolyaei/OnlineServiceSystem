package ir.maktab.data.repository;

import ir.maktab.data.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends PagingAndSortingRepository<Offer, Integer>, JpaSpecificationExecutor<Offer> {

    Optional<Offer> findOne(Specification<Offer> spec);

    List<Offer> findAll(Specification<Offer> spec);

    Page<Offer> findAll(Specification<Offer> spec, Pageable pageable);

}