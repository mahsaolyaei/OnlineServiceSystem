package ir.maktab.service;

import ir.maktab.dao.*;
import ir.maktab.dto.UserDto;
import ir.maktab.model.*;
import ir.maktab.model.enums.*;

import java.util.Date;
import java.util.List;

public class CustomerService extends UserService {

    private final CommentDao commentDao;

    public CustomerService() {
        super();
        commentDao = new CommentDao();
    }

    public UserDto login(String email, String password) {
        return login(email, password, UserType.CUSTOMER);
    }

    public Order addOrder(UserDto userDto, SubService subService, String address, String description, long price, Date date) {
        User user = new User();
        user.setId(userDto.getId());
        Order order = Order.builder()
                .customer(user).subService(subService)
                .address(address).description(description)
                .offeredPrice(price).requestedDate(date)
                .status(OrderStatus.WAITING_FOR_EXPERTS_OFFERS)
                .build();
        orderDao.save(order);
        return order;
    }

    public void addComment(Order order, int score, String description) {
        Offer offer = offerDao.findAcceptedOffer(order);
        Comment comment = Comment.builder()
                .order(order).score(score).description(description).expert(offer.getExpert())
                .build();
        commentDao.save(comment);
    }

    public void payOrder(Order order) {
        order.setStatus(OrderStatus.PAID);
        orderDao.update(order);
        Offer offer = offerDao.findAcceptedOffer(order);
        updateWallet(UserType.CUSTOMER, order.getCustomer(), offer.getPrice());
        updateWallet(UserType.EXPERT, offer.getExpert(), offer.getPrice());
    }

    public List<Order> getUserOrdersByType(UserDto userDto, OrderStatus orderStatus) {
        User user = new User();
        user.setId(userDto.getId());
        return orderDao.findOrdersByUserAndStatus(user, orderStatus);
    }

    public List<Offer> getOffers(Order order) {
        return offerDao.findByOrderId(order.getId());
    }

    public void acceptOffer(Offer offer) {
        offer.setStatus(OfferStatus.ACCEPTED);
        offer.getOrder().setStatus(OrderStatus.WAITING_FOR_COMING_EXPERT);
        offerDao.update(offer);
    }

}
