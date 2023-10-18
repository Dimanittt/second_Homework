package dao;

import entity.Forecast;
import entity.GeoData;
import exception.DaoException;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GeoDataDao {

    private static final GeoDataDao INSTANCE = new GeoDataDao();
//
//    private static final String DELETE = """
//                DELETE FROM geodata
//                WHERE id = ?;
//            """;
//
//    private static final String SAVE = """
//            INSERT INTO geodata (longitude, latitude, city, country)
//            VALUES (?, ?, ?, ?)
//            """;
//
//    private GeoDataDao() {
//    }
//
//    public static GeoDataDao getInstance() {
//        return INSTANCE;
//    }
//
//    public boolean delete(int id) {
//        try (Connection connection = ConnectionManager.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
//            preparedStatement.setInt(1, id);
//            return preparedStatement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
//    }
//
//    public void save(GeoData geoData) {
//        try (Connection connection = ConnectionManager.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
//
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
//    }

}
