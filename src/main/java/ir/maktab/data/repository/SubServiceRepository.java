package ir.maktab.data.repository;

import ir.maktab.data.model.Service;
import ir.maktab.data.model.SubService;
import ir.maktab.data.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServiceRepository extends CrudRepository<SubService, Integer> {

    Optional<SubService> findByName(String name);

    List<SubService> findByService(Service service);
}