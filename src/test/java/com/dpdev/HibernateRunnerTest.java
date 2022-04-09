package com.dpdev;

import com.dpdev.entity.Company;
import com.dpdev.entity.User;
import com.dpdev.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Cleanup;

class HibernateRunnerTest {

    @Test
    void checkOrphanRemoval() {
        Company company = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.getReference(Company.class, 1);
            company.getUsers().removeIf(user -> user.getId().equals(8L));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 1);

            session.getTransaction().commit();
        }
        var users = company.getUsers();
        System.out.println(users.size());
        /**
         *TODO in line 52 we have hibernate.LazyInitializationException
         *TODO because the session was closed
         **/

    }

    @Test
    void deleteUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

//        var company = session.get(Company.class, 11);
//        session.delete(company);

        var user = session.get(User.class, 1L);
        session.delete(user);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = Company.builder()
            .name("Facebook")
            .build();

        var user = User.builder()
            .username("sveta@gmail.com")
            .build();

        company.addUser(user);

        session.save(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        var company = session.get(Company.class, 1);
        System.out.println("");
        session.getTransaction().commit();
    }

    @Test
    void heckGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("lastname");
        resultSet.getString("firstname");

        Class<User> clazz = User.class;
        var constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        var usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));



    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .build();
        String sql = """
                INSERT 
                INTO 
                %s
                (%s)
                VALUES
                (%s)
                """;
        var tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());
        var declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));
        var columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));
        System.out.println(sql.formatted(tableName, columnNames, columnValues));
//        Connection connection = null;
//        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
//        for (Field declaredField : declaredFields) {
//            declaredField.setAccessible(true);
//            preparedStatement.setObject(1, declaredField.get(user));
    }
}