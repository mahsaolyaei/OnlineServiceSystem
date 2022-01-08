package ir.maktab.dao;

import ir.maktab.model.Service;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class ServiceDao extends GeneralDao {

    public List<Service> findAll() {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from TB_SERVICE s";
        Query query = getSession().createQuery(hql);
        List<Service> resultList = query.getResultList();
        transaction.commit();
        return resultList;
    }

    public Service findByName(String name) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from TB_SERVICE s where s.name = :name";
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        List<Service> resultList = query.getResultList();
        Service service = null;
        if (resultList != null && resultList.size() > 0)
            service = resultList.get(0);
        transaction.commit();
        return service;
    }

}
