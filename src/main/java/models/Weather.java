package models;

import java.util.List;

public class Weather {

    private int id;

    private GeoData geoData;

    private Forecast forecast;

    private List<User> user_requests;

    /**
     * DTO отвечающий за комбинацию {@link Forecast} и {@link GeoData}
     */
    public Weather() {
    }

    public Weather(GeoData geoData, Forecast forecast) {
        this.geoData = geoData;
        this.forecast = forecast;
    }

    public void addUser(User user) {
        user_requests.add(user);
        user.addWeather(this);
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

    public Forecast getForecast() {
        return forecast;
    }

    public List<User> getUser_requests() {
        return user_requests;
    }

    public void setUser_requests(List<User> user_requests) {
        this.user_requests = user_requests;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "geoData=" + geoData +
                ", forecast=" + forecast +
                '}';
    }
}
