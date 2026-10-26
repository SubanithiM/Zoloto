package com.zoloto.controller;

import java.io.IOException;

import com.zoloto.model.User;
import com.zoloto.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/users")
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;


    private UserService userService;


    /* ==========================================
       INIT
       ========================================== */

    @Override
    public void init() throws ServletException {

        userService = new UserService();

    }


    /* ==========================================
       GET
       ========================================== */

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        request.getRequestDispatcher("/register.jsp")
               .forward(request, response);

    }


    /* ==========================================
       POST
       ========================================== */

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        /* --------------------------------------
           Read form values
           -------------------------------------- */

        String firstName =
                request.getParameter("firstName");

        String lastName =
                request.getParameter("lastName");

        String email =
                request.getParameter("email");

        String mobile =
                request.getParameter("mobile");

        String password =
                request.getParameter("password");


        /* --------------------------------------
           Remove unnecessary spaces
           -------------------------------------- */

        if (firstName != null) {
            firstName = firstName.trim();
        }

        if (lastName != null) {
            lastName = lastName.trim();
        }

        if (email != null) {
            email = email.trim();
        }

        if (mobile != null) {
            mobile = mobile.trim();
        }


        /* --------------------------------------
           Check required fields
           -------------------------------------- */

        if (firstName == null
                || firstName.isEmpty()
                || lastName == null
                || lastName.isEmpty()
                || email == null
                || email.isEmpty()
                || mobile == null
                || mobile.isEmpty()
                || password == null
                || password.isEmpty()) {


            request.setAttribute(
                    "error",
                    "All fields are required."
            );


            request.getRequestDispatcher(
                    "/register.jsp")
                   .forward(request, response);


            return;
        }


        /* --------------------------------------
           Create User object
           -------------------------------------- */

        User user = new User();


        user.setFirstName(firstName);

        user.setLastName(lastName);

        user.setEmail(email);

        user.setMobile(mobile);

        user.setPassword(password);


        /* --------------------------------------
           Register user
           -------------------------------------- */

        boolean registered =
                userService.registerUser(user);


        /* --------------------------------------
           Registration SUCCESS
           -------------------------------------- */

        if (registered) {


            /*
             * After successful registration,
             * go to LOGIN page.
             */

            response.sendRedirect(
                    request.getContextPath()
                    + "/login?success=Registration successful! Please login to continue."
            );


            return;
        }


        /* --------------------------------------
           Registration FAILED
           -------------------------------------- */

        request.setAttribute(
                "error",
                "Registration failed. Email or mobile number may already exist."
        );


        request.getRequestDispatcher(
                "/register.jsp")
               .forward(request, response);

    }

}