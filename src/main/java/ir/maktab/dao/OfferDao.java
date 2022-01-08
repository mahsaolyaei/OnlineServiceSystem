package ir.maktab.dao;

import ir.maktab.model.Offer;
import ir.maktab.model.Order;
import ir.maktab.model.enums.OfferStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class OfferDao extends GeneralDao {

    public Offer findAcceptedOffer(Order order) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Offer.class, "o");
        criteria.add(Restrictions.eq("o.order.id", order.getId()));
        criteria.add(Restrictions.eq("o.status", OfferStatus.ACCEPTED));
        List<Offer> result = criteria.list();
        transaction.commit();
        return result.get(0);
    }

    public List<Offer> findByOrderId(int orderId) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Offer.class, "o");
        criteria.add(Restrictions.eq("o.order.id", orderId));
        List<Offer> result = criteria.list();
        transaction.commit();
        return result;
    }
}
