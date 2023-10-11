package servlets;

import service.WeatherService;
import dto.Weather;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/3day-period")
public class ThreeDayPeriod extends HttpServlet {
    WeatherService weatherService = new WeatherService("https://api.open-meteo.com/v1/forecast?latitude=53.3486&longitude=50.2108&hourly=temperature_2m,relativehumidity_2m,precipitation_probability&timezone=auto&forecast_days=3");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Weather weather = weatherService.getClassFromJson(weatherService.getJsonAsString());
        req.setAttribute("time", weather.getHourly().getTime());
        req.setAttribute("temperature", weather.getHourly().getTemperature2m());
        req.setAttribute("precipitation", weather.getHourly().getPrecipitationProbability());
        req.setAttribute("humidity", weather.getHourly().getRelativehumidity2m());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("3day-period.jsp");
        requestDispatcher.forward(req, resp);
    }
}
