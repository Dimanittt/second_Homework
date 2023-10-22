package entity;

import java.util.List;

public class User {

    private int id;

    private String hometown;

    private String username;

    private String password;

    private int userRole;

    private List<Weather> weatherRequests;

    public User() {
    }

    public User(int id, String hometown, String username, String password, int userRole, List<Weather> weatherRequests) {
        this.id = id;
        this.hometown = hometown;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.weatherRequests = weatherRequests;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public void addWeather(Weather weather) {
        weatherRequests.add(weather);
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

    public List<Weather> getWeatherRequests() {
        return weatherRequests;
    }

    public void setWeatherRequests(List<Weather> weatherRequests) {
        this.weatherRequests = weatherRequests;
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
               ", userRole=" + userRole +
               ", weatherRequests=" + weatherRequests +
               '}';
    }
}
