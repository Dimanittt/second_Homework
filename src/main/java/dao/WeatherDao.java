package dao;

import entity.Forecast;
import entity.Weather;
import utils.ConnectionManager;

import java.sql.*;

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
                       w.date
                FROM weather w
                         INNER JOIN geodata g ON g.id = w.geodata_id
                    AND g.city = ?
                    AND g.country = ?
                    AND w.date = ?
                            """;

    private static final String SAVE_TO_GET_ID = """
            INSERT INTO weather (date)
            VALUES (?)
            """;

    private static final String UPDATE_PARAMETERS = """
            UPDATE  weather
            SET forecast_id = ?,
                geodata_id = ?
            WHERE id = ?                 
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

            checkPreparedStatement.setString(1, weather.getGeoData().getCity());
            checkPreparedStatement.setString(2, weather.getGeoData().getCountry());
            checkPreparedStatement.setDate(3, weather.getDate());

            ResultSet checkResultSet = checkPreparedStatement.executeQuery();
            boolean check = true;

            if (checkResultSet.next()){
                if (checkResultSet.getString("city").equals(weather.getGeoData().getCity()) &&
                    checkResultSet.getString("country").equals(weather.getGeoData().getCountry()) &&
                    checkResultSet.getDate("date").equals(weather.getDate())) {
                    check = false;
                }
            }

            if (check) {

                getIdPreparedStatement.setDate(1, weather.getDate());
                getIdPreparedStatement.executeUpdate();

                ResultSet generatedKeys = getIdPreparedStatement.getGeneratedKeys();
                int weatherId = 0;
                if (generatedKeys.next()) {
                    weatherId = generatedKeys.getInt("id");
                }

                for (Forecast forecast : weather.getForecast()) {
                    forecast.setId(weatherId);
                }

                int geoDataId = geoDataDao.save(weather.getGeoData());

                forecastDao.save(weather.getForecast());

                savePreparedStatement.setInt(1, weatherId);
                savePreparedStatement.setInt(2, geoDataId);
                savePreparedStatement.setInt(3, weatherId);
                savePreparedStatement.executeUpdate();
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
}
