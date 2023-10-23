package servlets;

import dao.UserDao;
import dao.WeatherDao;
import entity.User;
import entity.Weather;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.WeatherService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Сервлет, отвечающий за логику валидации вводимых данных и формирования прогноза
 */
@WebServlet("/forecast")
public class ForecastServlet extends HttpServlet {

    UserDao userDao = UserDao.getInstance();

    WeatherDao weatherDao = WeatherDao.getInstance();

    WeatherService weatherService = WeatherService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Weather> weather = null;
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
        weather.stream().forEach(x -> {
            try {
                x.setUserId(user.getId());
                weatherDao.save(x);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        User updatedWeatherListUser = userDao.getByUsernameAndPassword(user.getUsername(), user.getPassword()).get();
        request.getSession().setAttribute("user", updatedWeatherListUser);
        request.setAttribute("weather", weather);
        RequestDispatcher dispatcher = request.getRequestDispatcher("forecast.jsp");
        dispatcher.forward(request, response);
    }
}
