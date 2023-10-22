package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AuthorizationService;

import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    AuthorizationService authorizationService = AuthorizationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("register.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (authorizationService.register(username, password)) {
            resp.sendRedirect("/login?registerSuccess&username=" + username);
        } else {
            resp.sendRedirect("/register?error");
        }
    }
}
