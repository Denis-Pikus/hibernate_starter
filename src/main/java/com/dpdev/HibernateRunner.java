package com.dpdev;

import java.time.LocalDate;

import com.dpdev.entity.Birthday;
import com.dpdev.entity.Company;
import com.dpdev.entity.PersonalInfo;
import com.dpdev.entity.User;
import com.dpdev.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws HibernateException {
        Company company = Company.builder()
            .name("Google")
            .build();

//        User user1 = User.builder()
//            .username("vas@mail.ru")
//            .personalInfo(PersonalInfo.builder()
//                .lastname("Vasya")
//                .firstname("VAs")
//                .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
//                .build())
//            .company(company)
//            .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                //User user1 = session1.get(User.class, 2L);
                session1.save(company);
                //var company1 = session1.get(Company.class, 1);
                //user1.setCompany(company1);
//                session1.save(user1);

                session1.getTransaction().commit();
            }
        }
    }
}
