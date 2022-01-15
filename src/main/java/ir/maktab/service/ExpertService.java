package ir.maktab.service;

import ir.maktab.data.model.Offer;
import ir.maktab.data.model.Order;
import ir.maktab.data.model.User;
import ir.maktab.data.model.enums.OfferStatus;
import ir.maktab.data.model.enums.OrderStatus;
import ir.maktab.data.model.enums.UserType;
import ir.maktab.exceptions.ValueMismatchException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExpertService extends UserService {

    public User login(String email, String password) {
        return login(email, password, UserType.EXPERT);
    }

    public List<Order> getOpenOrders(User userDto) {
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.WAITING_FOR_EXPERTS_OFFERS);
        statuses.add(OrderStatus.WAITING_FOR_SELECT_EXPERT);
        return orderRepository.findAll(addExpertIdAndOrderStatusConditions(userDto.getId(), statuses));
    }

    public void addOffer(User userDto, Order order, long price, Date date, int duration) {
        /*Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if (!userOptional.isPresent())
            throw new DataNotFoundException("User Not Found!");
        User user = userOptional.get();*/
        if (price < order.getOfferedPrice())
            throw new ValueMismatchException("Offered Value Can't Be Less Than " + order.getOfferedPrice());
        order.setStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
        Offer offer = Offer.builder()
                .expert(userDto).order(order).price(price)
                .respondDate(date).hours(duration)
                .status(OfferStatus.PENDING)
                .build();
        offerRepository.save(offer);
        orderRepository.save(order);
    }
}
