package ir.maktab.service;

import ir.maktab.data.model.*;
import ir.maktab.data.model.enums.OfferStatus;
import ir.maktab.data.model.enums.OrderStatus;
import ir.maktab.data.model.enums.TransactionType;
import ir.maktab.data.model.enums.UserType;
import ir.maktab.data.repository.*;
import ir.maktab.exceptions.UserLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserService {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ServiceRepository serviceRepository;
    @Autowired
    protected SubServiceRepository subServiceRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OfferRepository offerRepository;
    @Autowired
    protected WalletRepository walletRepository;
    @Autowired
    protected TransactionRepository transactionRepository;

    protected User login(String email, String password, UserType userType) {
        Optional<User> userOptional = userRepository.findOne(createLoginSpecification(email, password, userType));
        if (!userOptional.isPresent())
            throw new UserLoginException("User Name Or Password Is Wrong!");
        return userOptional.get();
    }

    public void changeUserPassword(User userDto, String password1) {
        User user = User.builder()
                .name(userDto.getName()).family(userDto.getFamily())
                .status(userDto.getStatus()).type(userDto.getType())
                .imageAddress(userDto.getImageAddress()).email(userDto.getEmail())
                .createdDate(userDto.getCreatedDate())
                .password(password1)
                .build();
        user.setId(userDto.getId());
        userRepository.save(user);
    }

    protected void updateWallet(UserType userType, User user, long price) {
        Optional<Wallet> walletOptional = walletRepository.findByUser(user);
        if (!walletOptional.isPresent())
            throw new UserLoginException("User Name Or Password Is Wrong!");
        Wallet wallet = walletOptional.get();
        long newWalletAmount;
        TransactionType transactionType;
        if (UserType.CUSTOMER.equals(userType)) {
            newWalletAmount = wallet.getAmount() - price;
            transactionType = TransactionType.WITHDRAW;
        }
        else {
            newWalletAmount = wallet.getAmount() + price;
            transactionType = TransactionType.DEPOSIT;
        }
        wallet.setAmount(newWalletAmount);
        Transaction transaction = Transaction.builder()
                .wallet(wallet).transactionAmount(price)
                .transactionType(transactionType)
                .build();
        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    static Specification<User> createLoginSpecification(String email, String password, UserType type) {
        return (root, query, criteriaBuilder) -> {
            Predicate emailCondition = criteriaBuilder.equal(root.get("email"), email);
            Predicate passwordCondition = criteriaBuilder.equal(root.get("password"), password);
            Predicate userTypeCondition = criteriaBuilder.equal(root.get("type"), type);
            return criteriaBuilder.and(emailCondition, passwordCondition, userTypeCondition);
        };
    }

    static Specification<Offer> addOrderIdConditions(Integer orderId) {
        return (root, query, criteriaBuilder) -> {
            Predicate orderCondition = criteriaBuilder.equal(root.join("order").get("id"), orderId);
            return criteriaBuilder.and(orderCondition);
        };
    }

    static Specification<Offer> addOrderIdConditionAndSorting(Integer orderId, String sorting) {
        return (root, query, criteriaBuilder) -> {
            if ("score".equals(sorting)) {
                Join<Offer, User> expert = root.join("expert");
                query.orderBy(criteriaBuilder.desc(expert.get(sorting)));
            }
            else if ("price".equals(sorting)) {
                query.orderBy(criteriaBuilder.asc(root.get(sorting)));
            }
            Predicate orderCondition = criteriaBuilder.equal(root.join("order").get("id"), orderId);
            return criteriaBuilder.and(orderCondition);
        };
    }

    static Specification<Offer> addOrderIdAndOfferStatusConditions(Integer orderId, OfferStatus offerStatus) {
        return (root, query, criteriaBuilder) -> {
            Predicate offerStatusCondition = criteriaBuilder.equal(root.get("status"), offerStatus);
            Predicate orderCondition = criteriaBuilder.equal(root.join("order").get("id"), orderId);
            return criteriaBuilder.and(offerStatusCondition, orderCondition);
        };
    }

    static Specification<Order> addCustomerIdAndOrderStatusConditions(Integer userId, List<OrderStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            Predicate orderStatusCondition = root.get("status").in(statuses);
            Predicate customerCondition = criteriaBuilder.equal(root.join("customer").get("id"), userId);
            return criteriaBuilder.and(orderStatusCondition, customerCondition);
        };
    }

    static Specification<Order> addExpertIdAndOrderStatusConditions(Integer userId, List<OrderStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            Predicate orderStatusCondition = root.get("status").in(statuses);
            Predicate expertCondition = criteriaBuilder.equal(root.join("subService").join("service").join("experts").get("id"), userId);
            return criteriaBuilder.and(orderStatusCondition, expertCondition);
        };
    }
}
