package models;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

/**
 * DTO отвечающий за прогноз погоды за определенный промежуток дней (1-16 исходя из возможностей <a href="https://open-meteo.com/">API</a>)
 */
public class Forecast {

    private int id;

    /**
     * Время в формате ISO 8601 с точностью до часа
     */
    @JsonSetter("time")
    private List<String> time;

    /**
     * Температура в цельсиях по часам
     */
    @JsonSetter("temperature_2m")
    private List<Double> temperature;

    /**
     * Относительная влажность в % по часам
     */
    @JsonSetter("relativehumidity_2m")
    private List<Integer> humidity;

    /**
     * Вероятность осадков в % по часам
     */
    @JsonSetter("precipitation_probability")
    private List<Integer> precipitationProbability;

    public Forecast() {
    }

    public Forecast(List<String> time, List<Double> temperature, List<Integer> humidity, List<Integer> precipitationProbability) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitationProbability = precipitationProbability;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Double> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Double> temperature) {
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getHumidity() {
        return humidity;
    }

    public void setHumidity(List<Integer> humidity) {
        this.humidity = humidity;
    }

    public List<Integer> getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(List<Integer> precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "time=" + time +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipitationProbability=" + precipitationProbability +
                '}';
    }
}

