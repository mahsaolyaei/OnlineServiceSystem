package ir.maktab.dao;

import ir.maktab.model.General;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import static ir.maktab.dao.SessionFactoryManager.getSessionFactory;

public class GeneralDao {
    protected Session getSession() {
        SessionFactory sessionFactory = getSessionFactory();
        return sessionFactory.getCurrentSession();
    }

    public void save(General general) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        int id = (int) session.save(general);
        general.setId(id);
        transaction.commit();
    }

    public void update(General general) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.update(general);
        transaction.commit();
    }
}
