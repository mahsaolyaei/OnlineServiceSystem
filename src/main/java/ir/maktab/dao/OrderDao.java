package ir.maktab.dao;

import ir.maktab.dto.UserDto;
import ir.maktab.model.Order;
import ir.maktab.model.User;
import ir.maktab.model.enums.OrderStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import java.util.ArrayList;
import java.util.List;

public class OrderDao extends GeneralDao {

    public List<Order> findOrdersByUserAndStatus(User user, OrderStatus orderStatus) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class, "o");
        criteria.add(Restrictions.eq("o.customer.id", user.getId()));
        criteria.add(Restrictions.eq("o.status", orderStatus));
        List<Order> result = criteria.list();
        transaction.commit();
        return result;
    }

    public List<Order> findOpenOrders(UserDto userDto) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class, "o");
        criteria.createAlias("o.subService", "ss", JoinType.INNER_JOIN);
        criteria.createAlias("ss.service", "s", JoinType.INNER_JOIN);
        criteria.createAlias("s.experts", "u", JoinType.INNER_JOIN);
//        criteria.createAlias("o.subService.service.experts", "u", JoinType.INNER_JOIN);
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.WAITING_FOR_EXPERTS_OFFERS);
        statuses.add(OrderStatus.WAITING_FOR_SELECT_EXPERT);
        criteria.add(Restrictions.in("o.status", statuses));
        criteria.add(Restrictions.eq("u.id", userDto.getId()));
        List<Order> result = criteria.list();
        transaction.commit();
        return result;
    }
}
