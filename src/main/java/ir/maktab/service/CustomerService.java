package ir.maktab.service;

import ir.maktab.data.model.*;
import ir.maktab.data.model.enums.OfferStatus;
import ir.maktab.data.model.enums.OrderStatus;
import ir.maktab.data.model.enums.UserType;
import ir.maktab.data.repository.CommentRepository;
import ir.maktab.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService extends UserService {

    @Autowired
    private CommentRepository commentRepository;

    public User login(String email, String password) {
        return login(email, password, UserType.CUSTOMER);
    }

    public void addOrder(User userDto, SubService subService, String address, String description, long price, Date date) {
        /*Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if (!userOptional.isPresent())
            throw new DataNotFoundException("User Not Found!");
        User user = userOptional.get();*/
        Order order = Order.builder()
                .customer(userDto).subService(subService)
                .address(address).description(description)
                .offeredPrice(price).requestedDate(date)
                .status(OrderStatus.WAITING_FOR_EXPERTS_OFFERS)
                .build();
        orderRepository.save(order);
    }

    public void addComment(Order order, int score, String description) {
        Optional<Offer> optionalOffer = offerRepository.findOne(addOrderIdAndOfferStatusConditions(order.getId(), OfferStatus.ACCEPTED));
        if (!optionalOffer.isPresent())
            throw new DataNotFoundException("Related Offer Not Found!");
        Offer offer = optionalOffer.get();
        Comment comment = Comment.builder()
                .order(order).score(score).description(description).expert(offer.getExpert())
                .build();
        commentRepository.save(comment);
    }

    public void payOrder(Order order) {
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        Optional<Offer> optionalOffer = offerRepository.findOne(addOrderIdAndOfferStatusConditions(order.getId(), OfferStatus.ACCEPTED));
        if (!optionalOffer.isPresent())
            throw new DataNotFoundException("Related Offer Not Found!");
        Offer offer = optionalOffer.get();
        updateWallet(UserType.CUSTOMER, order.getCustomer(), offer.getPrice());
        updateWallet(UserType.EXPERT, offer.getExpert(), offer.getPrice());
    }

    public List<Order> getUserOrdersByType(User userDto, OrderStatus orderStatus) {
        return orderRepository.findAll(addCustomerIdAndOrderStatusConditions(userDto.getId(), Collections.singletonList(orderStatus)));
    }

    public Page<Offer> getOffers(int pageSize, int pageNum, String sorting, Order order) {
        return offerRepository.findAll(addOrderIdConditionAndSorting(order.getId(), sorting), PageRequest.of(pageNum, pageSize));
    }

    public void acceptOffer(Offer offer) {
        offer.setStatus(OfferStatus.ACCEPTED);
        offer.getOrder().setStatus(OrderStatus.WAITING_FOR_COMING_EXPERT);
        offerRepository.save(offer);
    }
}
