package ir.maktab.service;

import ir.maktab.dto.UserDto;
import ir.maktab.model.Offer;
import ir.maktab.model.Order;
import ir.maktab.model.User;
import ir.maktab.model.enums.OfferStatus;
import ir.maktab.model.enums.OrderStatus;
import ir.maktab.model.enums.UserType;

import java.util.Date;
import java.util.List;

public class ExpertService extends UserService {

    public UserDto login(String email, String password) {
        return login(email, password, UserType.EXPERT);
    }

    public List<Order> getOpenOrders(UserDto userDto) {
        return orderDao.findOpenOrders(userDto);
    }

    public Offer addOffer(UserDto userDto, Order order, long price, Date date, int duration) {
        User user = new User();
        user.setId(userDto.getId());
        order.setStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
        Offer offer = Offer.builder()
                .expert(user).order(order).price(price)
                .respondDate(date).hours(duration)
                .status(OfferStatus.PENDING)
                .build();
        offerDao.save(offer);
        orderDao.update(order);
        return offer;
    }
}
