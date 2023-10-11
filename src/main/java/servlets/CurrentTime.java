package servlets;

import service.WeatherService;
import dto.Weather;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/current-time")
public class CurrentTime extends HttpServlet {
    WeatherService weatherService = new WeatherService("https://api.open-meteo.com/v1/forecast?latitude=53.3486&longitude=50.2108&hourly=temperature_2m,relativehumidity_2m,precipitation_probability&timezone=auto&forecast_days=3");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Weather weather = weatherService.getClassFromJson(weatherService.getJsonAsString());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH");
        String s = currentTime.format(new Date(System.currentTimeMillis()));
        FileWriter fileWriter = new FileWriter(new File("download.txt"));
        fileWriter.write("123");
        fileWriter.close();
        for (int i = 0; i < 24; i++) {
            if (weather.getHourly().getTime().get(i).substring(11,13).equals(s)) {
                req.setAttribute("time", weather.getHourly().getTime().get(i));
                req.setAttribute("temperature", weather.getHourly().getTemperature2m().get(i));
                req.setAttribute("precipitation", weather.getHourly().getPrecipitationProbability().get(i));
                req.setAttribute("humidity", weather.getHourly().getRelativehumidity2m().get(i));
            }
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("current-time.jsp");
        requestDispatcher.forward(req, resp);
    }
}
