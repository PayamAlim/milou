package aut.ap.service;

import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.*;

public class UserService {
    public static User findByEmail(String email) {
        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select * from users " +
                                        "where email = :email", User.class)
                                .setParameter("email", email)
                                .getResultStream()
                                .findFirst()
                                .orElse(null));
    }

    public static void registerUser(String name, String email, String password) {
        if (findByEmail(email) != null)
            throw new IllegalArgumentException("Such email already exists");

        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");

        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");

        checkPassword(password);

        User user = new User(name, email, password);

        SingletonSessionFactory.get()
                .inTransaction(session ->
                        session.persist(user));
    }

    public static User loginUser(String email, String password) {
        checkPassword(password);

        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");

        if (findByEmail(email) == null)
            throw new IllegalArgumentException("No such email.\nPlease sign up first!");

        if (!findByEmail(email).getPassword().equals(password))
            throw new IllegalArgumentException("Wrong password");

        return findByEmail(email);
    }

    private static void checkPassword(String password) {
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");
        if (password.length() < 8)
            throw new IllegalArgumentException("Short password");
    }
}
