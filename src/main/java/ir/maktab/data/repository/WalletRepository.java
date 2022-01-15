package ir.maktab.data.repository;

import ir.maktab.data.model.User;
import ir.maktab.data.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Integer> {

    Optional<Wallet> findByUser(User user);
}