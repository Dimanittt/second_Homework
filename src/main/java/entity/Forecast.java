package entity;


import java.sql.Date;
import java.sql.Time;

/**
 * DTO отвечающий за прогноз погоды за определенный промежуток дней (1-16 исходя из возможностей <a href="https://open-meteo.com/">API</a>)
 */
public class Forecast {

    private int weatherId;

    /**
     * Время в формате ISO 8601 с точностью до часа
     */
    private Time time;

    private Date date;

    /**
     * Температура в цельсиях по часам
     */
    private Double temperature;

    /**
     * Относительная влажность в % по часам
     */
    private Integer humidity;

    /**
     * Вероятность осадков в % по часам
     */
    private Integer precipitationProbability;

    public Forecast() {
    }

    public Forecast(int weatherId, Time time, Date date, Double temperature, Integer humidity, Integer precipitationProbability) {
        this.weatherId = weatherId;
        this.time = time;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitationProbability = precipitationProbability;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(Integer precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    @Override
    public String toString() {
        return "Forecast{" +
               "id=" + weatherId +
               ", time=" + time +
               ", temperature=" + temperature +
               ", humidity=" + humidity +
               ", precipitationProbability=" + precipitationProbability +
               '}';
    }
}

