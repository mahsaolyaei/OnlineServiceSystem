package ir.maktab.data.repository;

import ir.maktab.data.model.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Integer> {

    List<Service> findAll();

    Optional<Service> findByName(String name);

}