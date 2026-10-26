
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

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/register.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String firstName = trim(request.getParameter("firstName"));
        String lastName = trim(request.getParameter("lastName"));
        String email = trim(request.getParameter("email")).toLowerCase();
        String mobile = trim(request.getParameter("mobile"));
        String password = request.getParameter("password");
        String terms = request.getParameter("terms");

        if (firstName.isEmpty() || lastName.isEmpty()
                || email.isEmpty() || mobile.isEmpty()
                || password == null || password.isEmpty()) {
            showError(request, response, "Please fill in all required fields.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showError(request, response, "Please enter a valid email address.");
            return;
        }

        if (!mobile.matches("[0-9]{10}")) {
            showError(request, response, "Enter a valid 10-digit mobile number.");
            return;
        }

        if (password.length() < 6 || password.length() > 72) {
            showError(request, response,
                    "Password must contain 6 to 72 characters.");
            return;
        }

        if (!"on".equals(terms)) {
            showError(request, response,
                    "Please accept the terms and conditions.");
            return;
        }

        if (userDAO.existsByEmail(email)) {
            showError(request, response,
                    "An account with this email already exists.");
            return;
        }

        if (userDAO.existsByMobile(mobile)) {
            showError(request, response,
                    "An account with this mobile number already exists.");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = new User(
                firstName,
                lastName,
                email,
                mobile,
                hashedPassword,
                "CUSTOMER",
                "ACTIVE"
        );

        boolean created = userDAO.createUser(user);

        if (created) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/login?success=Account%20created%20successfully.%20Please%20sign%20in."
            );
        } else {
            showError(request, response,
                    "Registration failed. Please try again.");
        }
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           String message)
            throws ServletException, IOException {

        request.setAttribute("error", message);
        request.getRequestDispatcher("/register.jsp")
               .forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
