package servlets;

import models.Weather;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.WeatherService;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет, отвечающий за логику валидации вводимых данных и формирования прогноза
 */
@WebServlet("/forecast")
public class Forecast extends HttpServlet {

    WeatherService weatherService = new WeatherService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Weather weather = null;
        boolean cityValidation;
        boolean daysValidation;
        try {
            String city = request.getParameter("city");
            int days = Integer.parseInt(request.getParameter("days"));
            weather = weatherService.getWeatherFromCity(city, days);
        } catch (NullPointerException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException) {
                daysValidation = false;
                request.setAttribute("daysValidation", daysValidation);
            }
            if (e instanceof NullPointerException){
                cityValidation = false;
                request.setAttribute("cityValidation", cityValidation);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }
        List<Weather> weatherByDayList = weatherService.divideWeatherByDays(weather);
        request.setAttribute("weatherByDayList", weatherByDayList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("forecast.jsp");
        dispatcher.forward(request, response);
    }
}
