package services;

import dao.UserDao;
import entity.User;

import java.util.Optional;

public class AuthorizationService {

    UserDao userDao = UserDao.getInstance();

    private static final AuthorizationService INSTANCE = new AuthorizationService();

    private AuthorizationService(){}

    public static AuthorizationService getInstance(){
        return INSTANCE;
    }

    public Optional<User> login(String username, String password) {
        return userDao.getByUsernameAndPassword(username, password);
    }

    public boolean register(String username, String password) {
        return (userDao.save(username, password));
    }
}
