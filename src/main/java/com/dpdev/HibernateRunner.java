package com.dpdev;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {
    public static void main(String[] args) throws HibernateException {
        var configuration = new Configuration();
        configuration.configure();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            System.out.println("OK");
        }
    }
}
