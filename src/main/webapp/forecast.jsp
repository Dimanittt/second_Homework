<%@ page import="entity.Weather" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Forecast" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru_RU">
<head>
    <title>Погода в городе</title>
</head>
<body>
<br>
<button onclick="location.href='..'">На главную</button>
<br>
<% List<Weather> weatherByDayList = (List<Weather>) request.getAttribute("weather");
    if (!weatherByDayList.get(0).getGeoData().getUserDefinedCity().equals(weatherByDayList.get(0).getGeoData().getCity())) {
        out.print("<h2> Вы ввели " + weatherByDayList.get(0).getGeoData().getUserDefinedCity() + ", но мы подумали что это " + weatherByDayList.get(0).getGeoData().getCity() + "</h2>");
    }
%>
<h2>Погода в
    городе <% out.print(weatherByDayList.get(0).getGeoData().getCity() + ", " + weatherByDayList.get(0).getGeoData().getCountry()); %></h2>
<% for (Weather weather : weatherByDayList) { %>
<h3>На <% out.print(weather.getDate()); %></h3>
<table style="width:800px">
    <thead>
    <tr>
        <th>Время, ч</th>
        <th>Температура, ℃</th>
        <th>Относительная влажность, %</th>
        <th>Вероятность осадков, %</th>
    </tr>
    </thead>
    <tbody>
    <% for (Forecast forecast : weather.getForecast()) { %>
    <tr>
        <td><%= forecast.getTime() %>
        </td>
        <td><%= forecast.getTemperature() %>
        </td>
        <td><%= forecast.getHumidity() %>
        </td>
        <td><%= forecast.getPrecipitationProbability() %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>
</body>
</html>
