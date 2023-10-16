<%@ page import="models.Weather" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru_RU">
<head>
    <title>Погода в городе</title>
</head>
<body>
<br>
<button onclick="location.href='..'">На главную</button>
<br>
<% List<Weather> weatherByDayList = (List<Weather>) request.getAttribute("weatherByDayList");
    if (!weatherByDayList.get(0).getGeoData().getUserDefinedCity().equals(weatherByDayList.get(0).getGeoData().getCity())) {
        out.print("<h2> Вы ввели " + weatherByDayList.get(0).getGeoData().getUserDefinedCity() + ", но мы подумали что это " + weatherByDayList.get(0).getGeoData().getCity() + "</h2>");
    }
%>
<h2>Погода в
    городе <% out.print(weatherByDayList.get(0).getGeoData().getCity() + ", " + weatherByDayList.get(0).getGeoData().getCountry()); %></h2>
<% for (Weather weather : weatherByDayList) { %>
<h3>На <% out.print(weather.getForecast().getTime().get(0).substring(0, 10)); %></h3>
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
    <%
        Iterator<String> time = weather.getForecast().getTime().iterator();
        Iterator<Double> temperature = weather.getForecast().getTemperature().iterator();
        Iterator<Integer> humidity = weather.getForecast().getHumidity().iterator();
        Iterator<Integer> precipitationProbability = weather.getForecast().getPrecipitationProbability().iterator();
        while (time.hasNext() && temperature.hasNext() && humidity.hasNext() && precipitationProbability.hasNext()) { %>
    <tr>
        <td><%= time.next().substring(11, 13) %>
        </td>
        <td><%= temperature.next() %>
        </td>
        <td><%= humidity.next() %>
        </td>
        <td><%= precipitationProbability.next() %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>




</body>
</html>
