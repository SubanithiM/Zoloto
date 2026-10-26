package com.zoloto.controller;

import java.io.IOException;

import com.zoloto.dao.UserDAO;
import com.zoloto.dao.UserDAOImpl;
import com.zoloto.model.User;
import com.zoloto.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    // -----------------------------------------
    // GET - Open Login Page
    // -----------------------------------------

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }


    // -----------------------------------------
    // POST - Login
    // -----------------------------------------

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get form values

        String email = request.getParameter("email");
        String password = request.getParameter("password");


        // Remove unnecessary spaces

        if (email != null) {
            email = email.trim();
        }


        // -----------------------------------------
        // Basic validation
        // -----------------------------------------

        if (email == null
                || email.isEmpty()
                || password == null
                || password.isEmpty()) {

            request.setAttribute(
                    "error",
                    "Email and password are required.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);

            return;
        }


        // -----------------------------------------
        // Find user by email
        // -----------------------------------------

        User user = userDAO.findByEmail(email);


        // -----------------------------------------
        // Check whether user exists
        // -----------------------------------------

        if (user == null) {

            request.setAttribute(
                    "error",
                    "Invalid email or password.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);

            return;
        }


        // -----------------------------------------
        // Check account status
        // -----------------------------------------

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {

            request.setAttribute(
                    "error",
                    "Your account is inactive. Please contact support.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);

            return;
        }


        // -----------------------------------------
        // Verify password
        // -----------------------------------------

        boolean passwordCorrect =
                PasswordUtil.checkPassword(
                        password,
                        user.getPassword());


        if (!passwordCorrect) {

            request.setAttribute(
                    "error",
                    "Invalid email or password.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);

            return;
        }


        // -----------------------------------------
        // Login successful
        // -----------------------------------------

        HttpSession session = request.getSession();

        session.setAttribute("loggedInUser", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getFirstName());
        session.setAttribute("userRole", user.getRole());


        // -----------------------------------------
        // Remember Me
        // -----------------------------------------

        String rememberMe =
                request.getParameter("rememberMe");

        if ("on".equalsIgnoreCase(rememberMe)) {

            session.setMaxInactiveInterval(
                    7 * 24 * 60 * 60);

        } else {

            session.setMaxInactiveInterval(
                    30 * 60);
        }


        // -----------------------------------------
        // Redirect after successful login
        // -----------------------------------------

        response.sendRedirect(
                request.getContextPath() + "/home");

    }
}