package services;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.WeatherDao;
import entity.Forecast;
import entity.GeoData;
import entity.Weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис, отвечающий за взаимодействие entity с REST API сервисами (см. описание классов {@link GeoData} и {@link Forecast})
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

    private static final WeatherService INSTANCE = new WeatherService();

    private static final WeatherDao weatherDao = WeatherDao.getInstance();

    private WeatherService() {
    }

    public static WeatherService getInstance() {
        return INSTANCE;
    }

    /**
     * Использует внешний REST API от Яндекс геокодера для определения населенного пункта по введенному пользователем городу
     *
     * @param userDefinedCity введенный пользователем город
     * @return {@link GeoData}
     */
    private GeoData getGeoDataFromCity(String userDefinedCity) {

        GeoData geoData = new GeoData(userDefinedCity);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        String geoDataRequsetBuilderUri =
                yandexGeocoderApiUrl +
                "?" +
                yandexGeocoderApiKey +
                "&" +
                getYandexGeocoderApiSettings +
                userDefinedCity +
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

            return geoData;

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод обращается к внешнему REST API для получения прогноза погоды для населенного понкта для заданного количества дней
     * @param amountOfDays введенное пользователем количество дней
     * @param geoData полученный населенный пункт из метода {@link #getGeoDataFromCity(String)}
     * @return список прогноза погоды по часам
     */
    private List<Forecast> getForecastForAmountOfDays(int amountOfDays, GeoData geoData) {

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

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
                amountOfDays;

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

            List<Timestamp> timestamps = Arrays.asList(mapper.reader()
                    .forType(Timestamp[].class)
                    .readValue(forecastNode.at("/time")));

            List<Double> forecastTemperature = Arrays.asList(mapper.reader()
                    .forType(Double[].class)
                    .readValue(forecastNode.at("/temperature_2m")));

            List<Integer> forecastHumidity = Arrays.asList(mapper.reader()
                    .forType(Integer[].class)
                    .readValue(forecastNode.at("/relativehumidity_2m")));

            List<Integer> forecastPrecipitationProbability = Arrays.asList(mapper.reader()
                    .forType(Integer[].class)
                    .readValue(forecastNode.at("/precipitation_probability")));

            List<Forecast> forecastList = new ArrayList<>();

            for (int i = 0; i < timestamps.size(); i++) {
                Forecast forecast = new Forecast();
                forecast.setTime(Time.valueOf(timestamps.get(i).toLocalDateTime().toLocalTime()));
                forecast.setDate(Date.valueOf(timestamps.get(i).toLocalDateTime().toLocalDate()));
                forecast.setTemperature(forecastTemperature.get(i));
                forecast.setHumidity(forecastHumidity.get(i));
                forecast.setPrecipitationProbability(forecastPrecipitationProbability.get(i));
                forecastList.add(forecast);
            }
            return forecastList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param userDefinedCity вводимый пользователем город, который декодируется внутри метода
     * @param amountOfDays    вводимое пользователем количество дней, на которые требуется прогноз
     * @return {@link Weather}
     * @throws IllegalArgumentException в том случае, когда пользователь ввел некорректное количество дней, или ничего не ввел
     * @throws NullPointerException     в том случае, когда геокодеру не удалось распознать город
     */
    public List<Weather> getWeatherFromCity(String userDefinedCity, int amountOfDays) throws IllegalArgumentException, NullPointerException {

        if (amountOfDays <= 0 || amountOfDays >= 10) {
            throw new IllegalArgumentException();
        }

        GeoData geoData = getGeoDataFromCity(userDefinedCity);

        List<Forecast> forecastForDefinedAmountOfDays = getForecastForAmountOfDays(amountOfDays, geoData);

        Weather weatherToDivide = new Weather(geoData, forecastForDefinedAmountOfDays);

        List<Weather> resultWeatherArrayList = new ArrayList<>();
        int index = 0;
        do {
            Weather weather = new Weather(weatherToDivide.getGeoData(),
                    new ArrayList<>());
            List<Forecast> forecastForOneDay = new ArrayList<>();
            for (int i = index; i < index + 24; i++) {
                forecastForOneDay.add(weatherToDivide.getForecast().get(i));
            }
            weather.setDate(forecastForOneDay.get(0).getDate());
            weather.setForecast(forecastForOneDay);
            index += 24;
            resultWeatherArrayList.add(weather);
        } while (index < weatherToDivide.getForecast().size());

        return resultWeatherArrayList;
    }
}
