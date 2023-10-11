package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService extends AbstractService {
    private URI uri;
    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherService(String uri) {
        this.uri = URI.create(uri);
    }


    public String getJsonAsString() {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(this.uri)
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    public Weather getClassFromJson(String json) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
             Weather weather = mapper.readValue(json, Weather.class);
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        WeatherService service = new WeatherService("https://api.open-meteo.com/v1/forecast?latitude=53.3486&longitude=50.2108&current=temperature_2m&hourly=temperature_2m,relativehumidity_2m,precipitation_probability&timezone=auto&forecast_days=3");
        String json = service.getJsonAsString();
        System.out.println(service.getClassFromJson(json));
    }
}
