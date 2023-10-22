package dao;

import entity.Forecast;
import exception.DaoException;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForecastDao {

    private static final ForecastDao INSTANCE = new ForecastDao();

    private ForecastDao() {
    }

    static ForecastDao getInstance() {
        return INSTANCE;
    }

    private static final String DELETE = """
                DELETE FROM forecast
                WHERE id = ?;
            """;

    private static final String SAVE = """
            INSERT INTO forecast (id, hour, date, temperature, humidity, precipitation_probability)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID = """
            SELECT id,
            hour,
            temperature,
            humidity,
            precipitation_probability
            FROM forecast
            WHERE id = ?
            """;

    public List<Forecast> selectById(int id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Forecast> forecastList = new ArrayList<>();
            Forecast forecast = new Forecast();
            while (resultSet.next()) {
                forecast.setId(resultSet.getInt("id"));
                forecast.setTime(resultSet.getTime("hour"));
                forecast.setTemperature(resultSet.getDouble("temperature"));
                forecast.setHumidity(resultSet.getInt("humidity"));
                forecast.setPrecipitationProbability(resultSet.getInt("precipitation_probability"));
                forecastList.add(forecast);
            }
            return forecastList;
        } catch (
                SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(int id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void save(List<Forecast> forecastList) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            for (Forecast forecast : forecastList) {
                preparedStatement.setInt(1, forecast.getId());
                preparedStatement.setTime(2, forecast.getTime());
                preparedStatement.setDate(3, forecast.getDate());
                preparedStatement.setDouble(4, forecast.getTemperature());
                preparedStatement.setInt(5, forecast.getHumidity());
                preparedStatement.setInt(6, forecast.getPrecipitationProbability());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
