package ir.maktab.dao;

import ir.maktab.model.Wallet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class WalletDao extends GeneralDao {

    public Wallet findByUserId(int userId) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Wallet.class, "w");
        criteria.add(Restrictions.eq("w.user.id", userId));
        List<Wallet> result = criteria.list();
        transaction.commit();
        return result.get(0);
    }
}
