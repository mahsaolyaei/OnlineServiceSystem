package ir.maktab.data.repository;

import ir.maktab.data.model.Order;
import ir.maktab.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    List<Order> findAll(Specification<Order> spec);

}