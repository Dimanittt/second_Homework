package dao;

import entity.User;
import exception.DaoException;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {

    WeatherDao weatherDao = WeatherDao.getInstance();

    private static final UserDao INSTANCE = new UserDao();

    private UserDao() {
    }


    public static UserDao getInstance() {
        return INSTANCE;
    }

    private static final String SELECT_BY_USERNAME_AND_PASSWORD = """
            SELECT
                id,
                user_role,
                username,
                password,
                hometown
            FROM users
            WHERE username = ? AND password = ?
                """;

    private static final String INSERT_NEW_USER = """
            INSERT INTO users (user_role, username, password)
            VALUES (1, ?, ?)
            """;

    private static final String CHECK_USERNAME = """
            SELECT 
            username
            FROM users
            WHERE username = ?
            """;

    private static final String SELECT_WEATHER_REQUESTS = """
            SELECT 
            """;

    public Optional<User> getByUsernameAndPassword(String username, String password) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USERNAME_AND_PASSWORD)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setUserRole(resultSet.getInt("user_role"));
                user.setWeatherRequests(weatherDao.getAllUserWeatherRequests(resultSet.getInt("id")));
                user.setHometown(resultSet.getString("hometown"));
                return Optional.of(user);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean save(String username, String password) {
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement savePreparedStatement = connection.prepareStatement(INSERT_NEW_USER);
        PreparedStatement checkUsernamePreparedStatement = connection.prepareStatement(CHECK_USERNAME)) {
            checkUsernamePreparedStatement.setString(1, username);
            ResultSet resultSet = checkUsernamePreparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
            savePreparedStatement.setString(1, username);
            savePreparedStatement.setString(2, password);
            savePreparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
