package com.zoloto.controller;

import java.io.IOException;
import java.util.List;

import com.zoloto.dao.CategoryDAO;
import com.zoloto.dao.CategoryDAOImpl;
import com.zoloto.model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/categories")
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether user is logged in
        if (session == null
                || session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        // Get only ACTIVE categories
        List<Category> categories =
                categoryDAO.findByStatus("ACTIVE");

        request.setAttribute(
                "categories",
                categories);

        // Forward to categories.jsp
        request.getRequestDispatcher(
                "/categories.jsp")
                .forward(request, response);
    }
}