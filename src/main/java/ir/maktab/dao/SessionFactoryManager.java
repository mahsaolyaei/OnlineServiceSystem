package ir.maktab.dao;

import ir.maktab.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class SessionFactoryManager {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null) {
            Configuration configuration = new Configuration();
            Properties setting = new Properties();
            setting.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            setting.put(Environment.URL, "jdbc:mysql://localhost:3306/service18");
            setting.put(Environment.USER, "root");
            setting.put(Environment.PASS, "1234");
            setting.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            setting.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            setting.put(Environment.SHOW_SQL, "true");
            setting.put(Environment.FORMAT_SQL, "true");
            setting.put(Environment.HBM2DDL_AUTO, "update");
            setting.put(Environment.POOL_SIZE, "10");
            configuration.setProperties(setting);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Service.class);
            configuration.addAnnotatedClass(SubService.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(Offer.class);
            configuration.addAnnotatedClass(Wallet.class);
            configuration.addAnnotatedClass(Transaction.class);
            configuration.addAnnotatedClass(Comment.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}
