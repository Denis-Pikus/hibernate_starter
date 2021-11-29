package com.dpdev;

import com.dpdev.converter.BirthdayConverter;
import com.dpdev.entity.Birthday;
import com.dpdev.entity.Role;
import com.dpdev.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) throws HibernateException {
        var configuration = new Configuration();
       // configuration.addAnnotatedClass(User.class);

        configuration.addAttributeConverter(new BirthdayConverter(), true);
        configuration.configure();

        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var user = User.builder()
                    .username("ivan1@gmail.com")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                    .role(Role.ADMIN)
                    .build();
            session.save(user);
            session.getTransaction().commit();
        }
    }
}
