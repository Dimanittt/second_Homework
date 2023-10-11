package dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "temperature_2m",
        "relativehumidity_2m",
        "precipitation_probability"
})
public class Hourly {

    @JsonProperty("time")
    private List<String> time;
    @JsonProperty("temperature_2m")
    private List<Double> temperature2m;
    @JsonProperty("relativehumidity_2m")
    private List<Integer> relativehumidity2m;
    @JsonProperty("precipitation_probability")
    private List<Integer> precipitationProbability;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("time")
    public List<String> getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(List<String> time) {
        this.time = time;
    }

    @JsonProperty("temperature_2m")
    public List<Double> getTemperature2m() {
        return temperature2m;
    }

    @JsonProperty("temperature_2m")
    public void setTemperature2m(List<Double> temperature2m) {
        this.temperature2m = temperature2m;
    }

    @JsonProperty("relativehumidity_2m")
    public List<Integer> getRelativehumidity2m() {
        return relativehumidity2m;
    }

    @JsonProperty("relativehumidity_2m")
    public void setRelativehumidity2m(List<Integer> relativehumidity2m) {
        this.relativehumidity2m = relativehumidity2m;
    }

    @JsonProperty("precipitation_probability")
    public List<Integer> getPrecipitationProbability() {
        return precipitationProbability;
    }

    @JsonProperty("precipitation_probability")
    public void setPrecipitationProbability(List<Integer> precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Hourly{" +
                "time=" + time +
                ", temperature2m=" + temperature2m +
                ", relativehumidity2m=" + relativehumidity2m +
                ", precipitationProbability=" + precipitationProbability +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
