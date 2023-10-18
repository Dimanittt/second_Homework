package entity;

/**
 * DTO отвечающий за описание населенного пункта из <a href="https://yandex.ru/dev/maps/geocoder/">API</a>
 */
public class GeoData {

    private int id;

    /**
     * Широта
     */
    private double latitude;

    /**
     * Долгота
     */
    private double longitude;

    /**
     * Населенный пункт, определенный геокодером
     */
    private String city;

    /**
     * Столица населенного пункта
     */
    private String country;

    /**
     * Населенный пункт, введенный пользователем
     */
    private String userDefinedCity;

    public GeoData() {
    }

    public GeoData(String userDefinedCity) {
        this.userDefinedCity = userDefinedCity;
    }

    public GeoData(double latitude, double longitude, String city, String country, String userDefinedCity) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
        this.userDefinedCity = userDefinedCity;
    }

    public String getUserDefinedCity() {
        return userDefinedCity;
    }

    public void setUserDefinedCity(String userDefinedCity) {
        this.userDefinedCity = userDefinedCity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "GeoData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", userDefinedCity='" + userDefinedCity + '\'' +
                '}';
    }
}
