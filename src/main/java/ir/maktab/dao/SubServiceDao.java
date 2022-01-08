package ir.maktab.dao;

import ir.maktab.model.Service;
import ir.maktab.model.SubService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Query;
import java.util.List;

public class SubServiceDao extends GeneralDao {

    public SubService findByName(String name) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "from TB_SUB_SERVICE s where s.name = :name";
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        List<SubService> resultList = query.getResultList();
        SubService subService = null;
        if (resultList != null && resultList.size() > 0)
            subService = resultList.get(0);
        transaction.commit();
        return subService;
    }

    public List<SubService> findByService(Service service) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(SubService.class, "ss");
        criteria.add(Restrictions.eq("ss.service", service));
        List<SubService> result = criteria.list();
        transaction.commit();
        return result;
    }
}
