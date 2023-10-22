package dao;

import entity.GeoData;
import exception.DaoException;
import utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeoDataDao {

    private static final GeoDataDao INSTANCE = new GeoDataDao();

    private GeoDataDao() {
    }

    static GeoDataDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE = """
            INSERT INTO geodata (longitude, latitude, city, country)
            VALUES (?, ?, ?, ?)
            """;

    private static final String CHECK_BEFORE_SAVE = """
            SELECT
                city,
                country,
                id
            FROM geodata
            WHERE city = ? AND country = ?
            """;

    private static final String SELECT_GEODATA_BY_WEATHER_ID = """
            SELECT g.id,
                   g.city,
                   g.country,
                   g.latitude,
                   g.longitude
            FROM geodata g
                     INNER JOIN weather w ON g.id = w.geodata_id
                AND w.id = ?
                                    """;

    /**
     * Метод сначала проверяет наличие строки с параметрами city и country в таблице geodata,
     * если находит, то возвращает Id, если не находит, то создает строку в таблице geodata и возвращает ее Id
     *
     * @param geoData
     * @return Id строки в таблице geodata
     */
    public int save(GeoData geoData) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement savePreparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement checkPreparedStatement = connection.prepareStatement(CHECK_BEFORE_SAVE)) {

            checkPreparedStatement.setString(1, geoData.getCity());
            checkPreparedStatement.setString(2, geoData.getCountry());
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                savePreparedStatement.setDouble(1, geoData.getLongitude());
                savePreparedStatement.setDouble(2, geoData.getLatitude());
                savePreparedStatement.setString(3, geoData.getCity());
                savePreparedStatement.setString(4, geoData.getCountry());
                savePreparedStatement.executeUpdate();
                ResultSet generatedKeys = savePreparedStatement.getGeneratedKeys();
                int generatedId = 0;
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt("id");
                }
                return generatedId;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<GeoData> getGeoDataByWeatherId(int weatherId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GEODATA_BY_WEATHER_ID)) {
                preparedStatement.setInt(1, weatherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            GeoData geoData = null;
            if (resultSet.next()) {
                geoData = new GeoData();
                geoData.setId(resultSet.getInt("id"));
                geoData.setLatitude(resultSet.getDouble("latitude"));
                geoData.setLongitude(resultSet.getDouble("longitude"));
                geoData.setCountry(resultSet.getString("country"));
                geoData.setCity(resultSet.getString("city"));
                return Optional.of(geoData);
            }
            return Optional.ofNullable(geoData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
