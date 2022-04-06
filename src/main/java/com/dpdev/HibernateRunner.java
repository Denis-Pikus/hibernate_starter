package com.dpdev;

import java.time.LocalDate;

import com.dpdev.entity.Birthday;
import com.dpdev.entity.PersonalInfo;
import com.dpdev.entity.User;
import com.dpdev.util.HibernateUtil;

import org.hibernate.HibernateException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws HibernateException {

        User user = User.builder()
            .username("petr@mail.ru")
            .personalInfo(PersonalInfo.builder()
                .lastname("Petrov")
                .firstname("Petr")
                .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                .build())
            .build();
        log.info("User entity is in transient state, object: {}", user);

        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session1 = sessionFactory.openSession();
            try (session1) {
                var transaction = session1.beginTransaction();
                log.info("Transaction is created, {}", transaction);
                session1.saveOrUpdate(user);
                log.info("User is in persistent state, object: {}", user);
                session1.getTransaction().commit();
            }
            log.warn("User entity is in detached state: {}, session is closed {}", user, session1);
            try (var session = sessionFactory.openSession()) {
                PersonalInfo key = PersonalInfo.builder()
                    .lastname("Petrov")
                    .firstname("Petr")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                    .build();
                var user1 = session.get(User.class, key);
                System.out.println();
            }
        } catch (Exception exception) {
            log.error("Exception occurred", exception);
            throw exception;
        }
    }
}
