package dto;

public class Weather {

    private GeoData geoData;

    private Forecast forecast;

    public Weather() {
    }

    public Weather(GeoData geoData, Forecast forecast) {
        this.geoData = geoData;
        this.forecast = forecast;
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
