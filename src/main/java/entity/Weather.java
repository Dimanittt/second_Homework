package entity;

import java.sql.Date;
import java.util.List;

public class Weather {

    private int id;

    private GeoData geoData;

    private Date date;

    private List<Forecast> forecast;

    private int user_id;

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

    public Weather(int id, GeoData geoData, Date date, List<Forecast> forecast, int user_id) {
        this.id = id;
        this.geoData = geoData;
        this.date = date;
        this.forecast = forecast;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
               ", user_id=" + user_id +
               '}';
    }
}
