package ir.maktab.data.repository;

import ir.maktab.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);
}