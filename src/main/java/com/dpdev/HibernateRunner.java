package com.dpdev;

import java.time.LocalDate;

import com.dpdev.entity.Birthday;
import com.dpdev.entity.Company;
import com.dpdev.entity.PersonalInfo;
import com.dpdev.entity.User;
import com.dpdev.util.HibernateUtil;

import org.hibernate.HibernateException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws HibernateException {
        Company company = Company.builder()
            .name("Google")
            .build();

        User user = User.builder()
            .username("petr@mail.ru")
            .personalInfo(PersonalInfo.builder()
                .lastname("Petrov")
                .firstname("Petr")
                .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
                .build())
            .company(company)
            .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session1 = sessionFactory.openSession();
            try (session1) {
                var transaction = session1.beginTransaction();
                var user1 = session1.get(User.class, 1L);
                //                session1.save(company);
//                session1.saveOrUpdate(user);

                session1.getTransaction().commit();
            }
        }
    }
}
