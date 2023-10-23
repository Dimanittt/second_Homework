package dao;

import entity.Forecast;
import entity.Weather;
import utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherDao {

    ForecastDao forecastDao = ForecastDao.getInstance();
    GeoDataDao geoDataDao = GeoDataDao.getInstance();

    private static final WeatherDao INSTANCE = new WeatherDao();

    private WeatherDao() {
    }

    public static WeatherDao getInstance() {
        return INSTANCE;
    }

    private static final String CHECK_BEFORE_SAVE = """
            SELECT g.city,
                g.country,
                w.date,
                u.id
            FROM weather w
                INNER JOIN geodata g ON g.id = w.geodata_id
                INNER JOIN users u on u.id = w.user_id
            AND u.id = ?
            AND g.city = ?
            AND g.country = ?
            AND w.date = ?
                                                        """;

    private static final String SAVE_TO_GET_ID = """
            INSERT INTO weather (date, user_id)
            VALUES (?, ?)
            """;

    private static final String UPDATE_PARAMETERS = """
            UPDATE  weather
            SET 
                geodata_id = ?
            WHERE id = ?                 
            """;

    private static final String SELECT_USER_WEATHER_LIST = """
            SELECT
                id,
                date,
                geodata_id,
                user_id
            FROM weather
            WHERE user_id = ?
            """;


    public void save(Weather weather) throws SQLException {

        Connection connection = null;
        PreparedStatement savePreparedStatement = null;
        PreparedStatement getIdPreparedStatement = null;
        PreparedStatement checkPreparedStatement = null;

        try {

            connection = ConnectionManager.getConnection();
            savePreparedStatement = connection.prepareStatement(UPDATE_PARAMETERS);
            getIdPreparedStatement = connection.prepareStatement(SAVE_TO_GET_ID, Statement.RETURN_GENERATED_KEYS);
            checkPreparedStatement = connection.prepareStatement(CHECK_BEFORE_SAVE);

            connection.setAutoCommit(false);

            checkPreparedStatement.setInt(1, weather.getUserId());
            checkPreparedStatement.setString(2, weather.getGeoData().getCity());
            checkPreparedStatement.setString(3, weather.getGeoData().getCountry());
            checkPreparedStatement.setDate(4, weather.getDate());

            ResultSet checkResultSet = checkPreparedStatement.executeQuery();

            boolean check = true;

            if (checkResultSet.next()) {
                if (checkResultSet.getString("city").equals(weather.getGeoData().getCity()) &&
                    checkResultSet.getString("country").equals(weather.getGeoData().getCountry()) &&
                    checkResultSet.getInt("id") == weather.getUserId() &&
                    checkResultSet.getDate("date").equals(weather.getDate())) {
                    check = false;
                }
            }

            if (check) {

                getIdPreparedStatement.setDate(1, weather.getDate());
                getIdPreparedStatement.setInt(2, weather.getUserId());
                getIdPreparedStatement.executeUpdate();

                ResultSet generatedKeys = getIdPreparedStatement.getGeneratedKeys();
                int weatherId = 0;
                if (generatedKeys.next()) {
                    weatherId = generatedKeys.getInt("id");
                }

                for (Forecast forecast : weather.getForecast()) {
                    forecast.setWeatherId(weatherId);
                }

                int geoDataId = geoDataDao.save(weather.getGeoData(), connection);

                savePreparedStatement.setInt(1, geoDataId);
                savePreparedStatement.setInt(2, weatherId);
                savePreparedStatement.executeUpdate();

                forecastDao.save(weather.getForecast(), connection);

                connection.commit();

            }
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (savePreparedStatement != null) {
                savePreparedStatement.close();
            }
            if (getIdPreparedStatement != null) {
                getIdPreparedStatement.close();
            }
            if (checkPreparedStatement != null) {
                checkPreparedStatement.close();
            }
            connection.setAutoCommit(true);
        }
    }

    public List<Weather> getAllUserWeatherRequests(int userId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_WEATHER_LIST)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Weather> weatherList = new ArrayList<>();
            while (resultSet.next()) {
                Weather weather = new Weather();
                weather.setId(resultSet.getInt("id"));
                weather.setUserId(resultSet.getInt("user_id"));
                weather.setDate(resultSet.getDate("date"));
                weather.setGeoData(geoDataDao.getGeoDataByWeatherId(weather.getId()).get());
                weather.setForecast(forecastDao.selectById(weather.getId()));
                weatherList.add(weather);
            }
            return weatherList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
