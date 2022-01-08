package ir.maktab.service;

import ir.maktab.dao.*;
import ir.maktab.dto.UserDto;
import ir.maktab.model.*;
import ir.maktab.model.enums.TransactionType;
import ir.maktab.model.enums.UserType;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    protected final UserDao userDao;
    protected final ServiceDao serviceDao;
    protected final SubServiceDao subServiceDao;
    protected final OrderDao orderDao;
    protected final OfferDao offerDao;
    protected final WalletDao walletDao;
    protected final TransactionDao transactionDao;

    public UserService() {
        userDao = new UserDao();
        serviceDao = new ServiceDao();
        subServiceDao = new SubServiceDao();
        orderDao = new OrderDao();
        offerDao = new OfferDao();
        walletDao = new WalletDao();
        transactionDao = new TransactionDao();
    }

    protected UserDto login(String email, String password, UserType userType) {
        return userDao.login(email, password, userType);
    }

    public void changeUserPassword(UserDto userDto, String password1) {
        User user = User.builder()
                .name(userDto.getName()).family(userDto.getFamily())
                .status(userDto.getStatus()).type(userDto.getType())
                .imageAddress(userDto.getImageAddress()).email(userDto.getEmail())
                .createdDate(userDto.getCreatedDate())
                .password(password1)
                .build();
        user.setId(userDto.getId());
        userDao.update(user);
    }

    protected void updateWallet(UserType userType, User user, long price) {
        Wallet wallet = walletDao.findByUserId(user.getId());
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
        transactionDao.save(transaction);
        walletDao.update(wallet);
    }
}
