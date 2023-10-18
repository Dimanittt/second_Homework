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

    public static ForecastDao getInstance() {
        return INSTANCE;
    }

    private static final String DELETE = """
                DELETE FROM forecast
                WHERE id = ?;
            """;

    private static final String SAVE = """
            INSERT INTO forecast (id, hour, temperature, humidity, precipitation_probability)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE forecast
            SET hour = ?,
            temperature = ?,
            humidity = ?,
            precipitation_probability = ?
            WHERE id = ?
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

    private static final String SELECT_ALL_ID = """
            SELECT DISTINCT id
            FROM forecast
            """;

    public Forecast selectById(int id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Forecast forecast = new Forecast();
            forecast.setId(id);
            while (resultSet.next()) {
                forecast.setId(resultSet.getInt("id"));
                forecast.getTime().add(resultSet.getTimestamp("hour"));
                forecast.getTemperature().add(resultSet.getDouble("temperature"));
                forecast.getHumidity().add(resultSet.getInt("humidity"));
                forecast.getPrecipitationProbability().add(resultSet.getInt("precipitation_probability"));
            }
            return forecast;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Forecast> selectAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatementAllId = connection.prepareStatement(SELECT_ALL_ID)) {
            ResultSet resultSetId = preparedStatementAllId.executeQuery();
            List<Forecast> forecastList = new ArrayList<>();
            while (resultSetId.next()) {
                forecastList.add(selectById(resultSetId.getInt("id")));
            }
            return forecastList;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(Forecast forecast) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            for (int i = 0; i < 24; i++) {
                preparedStatement.setTimestamp(1, forecast.getTime().get(i));
                preparedStatement.setDouble(2, forecast.getTemperature().get(i));
                preparedStatement.setInt(3, forecast.getHumidity().get(i));
                preparedStatement.setInt(4, forecast.getPrecipitationProbability().get(i));
                preparedStatement.setInt(5, forecast.getId());
                preparedStatement.executeUpdate();
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
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

    public void save(Forecast forecast) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            for (int i = 0; i < 24; i++) {
                preparedStatement.setInt(1, forecast.getId());
                preparedStatement.setTimestamp(2, forecast.getTime().get(i));
                preparedStatement.setDouble(3, forecast.getTemperature().get(i));
                preparedStatement.setInt(4, forecast.getHumidity().get(i));
                preparedStatement.setInt(5, forecast.getPrecipitationProbability().get(i));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}
