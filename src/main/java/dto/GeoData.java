package dto;

public class GeoData {

    private double latitude;

    private double longitude;

    private String city;

    private String country;

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
