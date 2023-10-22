package entity;

import java.util.List;

public class User {

    private int id;

    private String hometown;

    private String username;

    private String password;

    private List<Weather> weather_requests;

    public User() {
    }

    public User(int id, String hometown, String username, String password, List<Weather> weather_requests) {
        this.id = id;
        this.hometown = hometown;
        this.username = username;
        this.password = password;
        this.weather_requests = weather_requests;
    }

    public void addWeather(Weather weather) {
        weather_requests.add(weather);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Weather> getWeather_requests() {
        return weather_requests;
    }

    public void setWeather_requests(List<Weather> weather_requests) {
        this.weather_requests = weather_requests;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", hometown='" + hometown + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", weather_requests=" + weather_requests +
               '}';
    }
}
