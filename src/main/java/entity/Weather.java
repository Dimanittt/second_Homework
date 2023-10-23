package entity;

import java.sql.Date;
import java.util.List;

public class Weather {

    private int id;

    private GeoData geoData;

    private Date date;

    private List<Forecast> forecast;

    private int userId;

    /**
     * DTO отвечающий за комбинацию {@link Forecast} и {@link GeoData}
     */
    public Weather() {
    }

    public Weather(GeoData geoData, List<Forecast> forecast) {
        this.geoData = geoData;
        this.forecast = forecast;
    }

    public Weather(GeoData geoData, Date date, List<Forecast> forecast) {
        this.geoData = geoData;
        this.date = date;
        this.forecast = forecast;
    }

    public Weather(int id, GeoData geoData, Date date, List<Forecast> forecast, int userId) {
        this.id = id;
        this.geoData = geoData;
        this.date = date;
        this.forecast = forecast;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(GeoData geoData) {
        this.geoData = geoData;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "Weather{" +
               "id=" + id +
               ", geoData=" + geoData +
               ", date=" + date +
               ", forecast=" + forecast +
               ", user_id=" + userId +
               '}';
    }
}
