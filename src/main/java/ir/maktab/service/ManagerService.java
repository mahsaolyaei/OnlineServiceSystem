package ir.maktab.service;

import ir.maktab.dto.UserDto;
import ir.maktab.exceptions.SimilarDataException;
import ir.maktab.model.Service;
import ir.maktab.model.SubService;
import ir.maktab.model.User;
import ir.maktab.model.Wallet;
import ir.maktab.model.enums.UserStatus;
import ir.maktab.model.enums.UserType;

import java.util.List;
import java.util.Map;

public class ManagerService extends UserService {

    public void addUser(UserType userType, String name, String family, String email, String password, String imageAddress) {
        if (userDao.findByEmail(email) != null)
            throw new SimilarDataException("Error: Email Exists.");
        User user = User.builder()
                .name(name).family(family)
                .email(email).password(password)
                .imageAddress(imageAddress)
                .type(userType).status(UserStatus.NEW)
                .build();
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        if (userType.equals(UserType.CUSTOMER))
            wallet.setAmount(1000000);
        else
            wallet.setAmount(0);
        userDao.save(user);
        walletDao.save(wallet);
    }

    public List<Service> getAllServices() {
        return serviceDao.findAll();
    }

    public List<SubService> getSubServices(Service service) {
        return subServiceDao.findByService(service);
    }

    public int getUsersSize(Map<String, Object> filterMap) {
        return userDao.calculateSearchSize(filterMap);
    }

    public List<UserDto> getUsers(int pageSize, int pageNum, Map<String, Object> filterMap, List<String> orderList) {
        return userDao.search(pageSize, pageNum, filterMap, orderList);
    }

    public Service addService(String name) {
        if (serviceDao.findByName(name) != null)
            throw new SimilarDataException("Error: Service Name Exists.");
        Service service = new Service();
        service.setName(name);
        serviceDao.save(service);
        return service;
    }

    public SubService addSubService(Service service, String name, long basePrice, String description) {
        if (subServiceDao.findByName(name) != null)
            throw new SimilarDataException("Error: Sub-Service Name Exists.");

        SubService subService = SubService.builder()
                .service(service).description(description)
                .name(name).basePrice(basePrice)
                .build();
        subServiceDao.save(subService);
        return subService;
    }

    public void updateService(Service service) {
        serviceDao.update(service);
    }

    public void addUserToService(Service service, UserDto userDto) {
        User user = userDao.findById(userDto.getId());
        service.getExperts().add(user);
        serviceDao.update(service);
    }
}
