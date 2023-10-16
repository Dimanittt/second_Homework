package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Forecast;
import models.GeoData;
import models.Weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис, отвечающий за взаимодействие DTO с REST API сервисами (см. описание классов {@link GeoData} и {@link Forecast})
 */
public class WeatherService {

    /**
     * URL - API геокодера
     */
    private final String yandexGeocoderApiUrl = "https://geocode-maps.yandex.ru/1.x/";

    /**
     * Личный ключ от геокодера
     */
    private final String yandexGeocoderApiKey = "apikey=1df00599-bdc9-4956-be67-1d0fb8391305";

    /**
     * kind=locality - обозначение парсинга населенного пункта,
     * results=1 - обозначает парсинг лишь 1 одного реузльтата, верного (если не указать, то будет 33 Москвы в респонсе))
     * geocode=? - заместо ? указывается город при составлении финального URL реквеста
     */
    private final String getYandexGeocoderApiSettings = "kind=locality&results=1&geocode=";

    /**
     * Формат данных, получаемый из респонса геокодера
     */
    private final String yandexGeocoderApiReturnFormat = "format=json";

    /**
     * URL - API сервиса погоды, бесплатный
     */
    private final String openMeteoApiUrl = "https://api.open-meteo.com/v1/forecast";

    /**
     * hourly=... - указание того, что получаем лист параметров по часам (параметры ниже)
     * temperature_2m - температура
     * relativehumidity_2m - относительная влажность
     * precipitation_probability - вероятность осадков
     * timezone=auto - автоматическое определение GMT по координатам
     */
    private final String openMeteoApiSettings = "hourly=temperature_2m,relativehumidity_2m,precipitation_probability&timezone=auto";

    /**
     * forecast_days=... - количество прогнозируемых дней
     */
    private final String openMeteoAmountOfForecastDays = "forecast_days=";

    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherService() {
    }

    /**
     *
     * @param city вводимый пользователем город, который декодируется внутри метода
     * @param countOfDays вводимое пользователем количество дней, на которые требуется прогноз
     * @return {@link Weather}
     * @throws IllegalArgumentException в том случае, когда пользователь ввел некорректное количество дней, или ничего не ввел
     * @throws ClassCastException в том случае, когда геокодеру не удалось распознать город
     */
    public Weather getWeatherFromCity(String city, int countOfDays) throws IllegalArgumentException, ClassCastException {

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (countOfDays < 1 || countOfDays > 16) {
            throw new IllegalArgumentException();
        }

        GeoData geoData = new GeoData(city);


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        String geoDataRequsetBuilderUri =
                yandexGeocoderApiUrl +
                        "?" +
                        yandexGeocoderApiKey +
                        "&" +
                        getYandexGeocoderApiSettings +
                        city +
                        "&" +
                        yandexGeocoderApiReturnFormat;

        HttpRequest geoDataRequest = HttpRequest.newBuilder()
                .uri(URI.create(geoDataRequsetBuilderUri))
                .GET()
                .build();

        HttpResponse<String> geoDataResponse;
        try {
            geoDataResponse = client.send(geoDataRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode geoDataCityNode = mapper.readTree(geoDataResponse.body())
                    .at("/response/GeoObjectCollection/featureMember")
                    .get(0)
                    .at("/GeoObject/name");

            JsonNode geoDataCountryNode = mapper.readTree(geoDataResponse.body())
                    .at("/response/GeoObjectCollection/featureMember")
                    .get(0)
                    .at("/GeoObject/metaDataProperty/GeocoderMetaData/AddressDetails/Country/CountryName");

            geoData.setCity(geoDataCityNode.asText());
            geoData.setCountry(geoDataCountryNode.asText());

            JsonNode geoDataCoordinatesNode = mapper.readTree(geoDataResponse.body())
                    .at("/response/GeoObjectCollection/featureMember")
                    .get(0)
                    .at("/GeoObject/Point");

            Matcher coordinateDecoding = Pattern.compile("-?\\d+.\\d+").matcher(geoDataCoordinatesNode.toString());
            coordinateDecoding.find();
            geoData.setLongitude(Double.parseDouble(coordinateDecoding.group()));
            coordinateDecoding.find();
            geoData.setLatitude(Double.parseDouble(coordinateDecoding.group()));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String forecastRequestBuilderUri =
                openMeteoApiUrl +
                        "?" +
                        openMeteoApiSettings +
                        "&" +
                        "latitude=" +
                        geoData.getLatitude() +
                        "&" +
                        "longitude=" +
                        geoData.getLongitude() +
                        "&" +
                        openMeteoAmountOfForecastDays +
                        countOfDays;

        HttpRequest forecastRequest = HttpRequest.newBuilder()
                .uri(URI.create(forecastRequestBuilderUri))
                .GET()
                .build();

        HttpResponse<String> forecastResponse = null;
        try {
            forecastResponse = client.send(forecastRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JsonNode forecastNode = mapper.readTree(forecastResponse.body()).at("/hourly");
            Forecast forecast = mapper.reader()
                    .forType(Forecast.class)
                    .readValue(forecastNode.toString());

            return new Weather(geoData, forecast);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Weather> divideWeatherByDays(Weather weatherToDivide) {
        List<Weather> resultWeatherArrayList = new ArrayList<>();
        int index = 0;
        do {
            Weather weather = new Weather(weatherToDivide.getGeoData(),
                    new Forecast(new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>()));
            for (int i = index; i <= index + 23; i++) {
                weather.getForecast().getHumidity().add(weatherToDivide.getForecast().getHumidity().get(i));
                weather.getForecast().getTime().add(weatherToDivide.getForecast().getTime().get(i));
                weather.getForecast().getTemperature().add(weatherToDivide.getForecast().getTemperature().get(i));
                weather.getForecast().getPrecipitationProbability().add(weatherToDivide.getForecast().getPrecipitationProbability().get(i));
            }
            index += 24;
            resultWeatherArrayList.add(weather);
        } while (index < weatherToDivide.getForecast().getTime().size());
        return resultWeatherArrayList;
    }
}
