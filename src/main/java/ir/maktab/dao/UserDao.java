package ir.maktab.dao;

import ir.maktab.dto.UserDto;
import ir.maktab.exceptions.UserLoginException;
import ir.maktab.model.Service;
import ir.maktab.model.User;
import ir.maktab.model.enums.UserType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao extends GeneralDao {

    public UserDto findByEmail(String email) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class, "u");
        criteria.add(Restrictions.eq("u.email", email).ignoreCase());
        criteria.setProjection(setProjections());
        criteria.setResultTransformer(Transformers.aliasToBean(UserDto.class));
        UserDto result = (UserDto) criteria.uniqueResult();
        transaction.commit();
        return result;
    }

    public User findById(int id) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class, "u");
        criteria.add(Restrictions.eq("u.id", id));
        User result = (User) criteria.uniqueResult();
        transaction.commit();
        return result;
    }

    public List<UserDto> findByService(Service service) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class, "u");
        criteria.createAlias("u.services", "s", JoinType.INNER_JOIN);
        criteria.add(Restrictions.eq("s.id", service.getId()));
        criteria.setProjection(setProjections());
        criteria.setResultTransformer(Transformers.aliasToBean(UserDto.class));
        List<UserDto> result = criteria.list();
        transaction.commit();
        return result;
    }

    public List<UserDto> findAll() {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class, "u");
        criteria.add(Restrictions.eq("u.type", UserType.EXPERT));
        criteria.setProjection(setProjections());
        criteria.setResultTransformer(Transformers.aliasToBean(UserDto.class));
        List<UserDto> result = criteria.list();
        transaction.commit();
        return result;
    }

    public int calculateSearchSize(Map<String, Object> filterMap) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = createCriteriaByRestrictions(session, filterMap, new ArrayList<>());
        List<UserDto> result = criteria.list();
        transaction.commit();
        return result.size();
    }

    public List<UserDto> search(int pageSize, int pageNum, Map<String, Object> filterMap, List<String> orderList) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = createCriteriaByRestrictions(session, filterMap, orderList);
        criteria.setFirstResult((pageNum - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        criteria.addOrder(Order.asc("id"));
        criteria.setProjection(setProjections());
        criteria.setResultTransformer(Transformers.aliasToBean(UserDto.class));
        List<UserDto> result = criteria.list();
        transaction.commit();
        return result;
    }

    private Criteria createCriteriaByRestrictions(Session session, Map<String, Object> filterMap, List<String> orderList) {
        Criteria criteria = session.createCriteria(User.class, "u");
        UserType userType = (UserType) filterMap.get("userType");
        String name = (String) filterMap.get("name");
        String family = (String) filterMap.get("family");
        String email = (String) filterMap.get("email");
        String password = (String) filterMap.get("password");
        String serviceName = (String) filterMap.get("serviceName");
        if (userType != null)
            criteria.add(Restrictions.eq("u.type", userType));
        if (name != null)
            criteria.add(Restrictions.like("u.name", name).ignoreCase());
        if (family != null)
            criteria.add(Restrictions.like("u.family", family).ignoreCase());
        if (email != null)
            criteria.add(Restrictions.like("u.email", email).ignoreCase());
        if (password != null)
            criteria.add(Restrictions.like("u.password", password));
        if (serviceName != null) {
            criteria.createAlias("u.services", "s", JoinType.INNER_JOIN);
            criteria.add(Restrictions.eq("s.name", serviceName).ignoreCase());
        }
        for (String orderItem: orderList) {
            criteria.addOrder(Order.asc(orderItem));
        }
        return criteria;
    }

    private Projection setProjections() {
        return Projections.projectionList()
                .add(Projections.property("u.id").as("id"))
                .add(Projections.property("u.name").as("name"))
                .add(Projections.property("u.family").as("family"))
                .add(Projections.property("u.email").as("email"))
                .add(Projections.property("u.imageAddress").as("imageAddress"))
                .add(Projections.property("u.score").as("score"))
                .add(Projections.property("u.type").as("type"))
                .add(Projections.property("u.status").as("status"))
                .add(Projections.property("u.createdDate").as("createdDate"));
    }

    public UserDto login(String email, String password, UserType userType) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("email", email);
        filterMap.put("password", password);
        filterMap.put("userType", userType);
        Criteria criteria = createCriteriaByRestrictions(session, filterMap, new ArrayList<>());
        criteria.setProjection(setProjections());
        criteria.setResultTransformer(Transformers.aliasToBean(UserDto.class));
        List<UserDto> result = criteria.list();
        transaction.commit();
        if (result.size() == 0)
            throw new UserLoginException("Email Or Password Is Wrong.");
        return result.get(0);
    }
}
