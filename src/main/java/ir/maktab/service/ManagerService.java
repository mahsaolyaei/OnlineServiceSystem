package ir.maktab.service;

import ir.maktab.data.model.Service;
import ir.maktab.data.model.SubService;
import ir.maktab.data.model.User;
import ir.maktab.data.model.Wallet;
import ir.maktab.data.model.enums.UserStatus;
import ir.maktab.data.model.enums.UserType;
import ir.maktab.exceptions.DataNotFoundException;
import ir.maktab.exceptions.SimilarDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ManagerService extends UserService {

    public Page<User> getUsers(int pageSize, int pageNum, Map<String, Object> filterMap) {
        return userRepository.findAll(addDynamicConditions(filterMap), PageRequest.of(pageNum, pageSize));
    }

    static Specification<User> addDynamicConditions(Map<String, Object> filterMap) {
        return (root, query, criteriaBuilder) -> {
            UserType userType = (UserType) filterMap.get("userType");
            String name = (String) filterMap.get("name");
            String family = (String) filterMap.get("family");
            String email = (String) filterMap.get("email");
            String password = (String) filterMap.get("password");
            String serviceName = (String) filterMap.get("serviceName");
            Predicate userTypeCondition = userType != null ? criteriaBuilder.equal(root.get("type"), userType) : null;
            Predicate nameCondition = name != null ? criteriaBuilder.equal(root.get("name"), name) : null;
            Predicate familyCondition = family != null ? criteriaBuilder.equal(root.get("family"), family) : null;
            Predicate emailCondition = email != null ? criteriaBuilder.equal(root.get("email"), email) : null;
            Predicate passwordCondition = password != null ? criteriaBuilder.equal(root.get("password"), password) : null;
            Predicate serviceNameCondition = serviceName != null ? criteriaBuilder.equal(root.join("services").get("name"), serviceName) : null;
            Predicate finalPredicate = null;
            if (userTypeCondition != null)
                finalPredicate = userTypeCondition;
            if (nameCondition != null)
                finalPredicate = finalPredicate == null? nameCondition: criteriaBuilder.and(finalPredicate, nameCondition);
            if (familyCondition != null)
                finalPredicate = finalPredicate == null? familyCondition: criteriaBuilder.and(finalPredicate, familyCondition);
            if (emailCondition != null)
                finalPredicate = finalPredicate == null? emailCondition: criteriaBuilder.and(finalPredicate, emailCondition);
            if (passwordCondition != null)
                finalPredicate = finalPredicate == null? passwordCondition: criteriaBuilder.and(finalPredicate, passwordCondition);
            if (serviceNameCondition != null)
                finalPredicate = finalPredicate == null? serviceNameCondition: criteriaBuilder.and(finalPredicate, serviceNameCondition);
            return finalPredicate;
        };
    }

    public void addUser(UserType userType, String name, String family, String email, String password, String imageAddress) {
        if (userRepository.findByEmail(email).isPresent())
            throw new SimilarDataException("Error: Email Exists.");
        User user = User.builder()
                .name(name).family(family)
                .email(email).password(password)
                .imageAddress(imageAddress)
                .type(userType).status(UserStatus.NEW)
                .build();
        User savedUser = userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        if (userType.equals(UserType.CUSTOMER))
            wallet.setAmount(1000000);
        else
            wallet.setAmount(0);
        walletRepository.save(wallet);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<SubService> getSubServices(Service service) {
        return subServiceRepository.findByService(service);
    }

    public void addService(String name) {
        if (serviceRepository.findByName(name).isPresent())
            throw new SimilarDataException("Error: Service Name Exists.");
        Service service = new Service();
        service.setName(name);
        serviceRepository.save(service);
    }

    public void addSubService(Service service, String name, long basePrice, String description) {
        if (subServiceRepository.findByName(name).isPresent())
            throw new SimilarDataException("Error: Sub-Service Name Exists.");

        SubService subService = SubService.builder()
                .service(service).description(description)
                .name(name).basePrice(basePrice)
                .build();
        subServiceRepository.save(subService);
    }

    public void updateService(Service service) {
        serviceRepository.save(service);
    }

    public void addUserToService(Service service, User userDto) {
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if (!userOptional.isPresent())
            throw new DataNotFoundException("User Not Found!");
        User user = userOptional.get();
        service.getExperts().add(user);
        serviceRepository.save(service);
    }
}
