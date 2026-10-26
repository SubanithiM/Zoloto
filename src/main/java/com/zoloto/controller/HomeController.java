
package com.zoloto.controller;

import java.io.IOException;
import java.util.List;

import com.zoloto.dao.ProductDAO;
import com.zoloto.dao.ProductDAOImpl;
import com.zoloto.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    // Maximum number of products to display on Home.
    private static final int PRODUCT_LIMIT = 50;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Check whether the user is logged in.
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        // Fetch up to 50 active products.
        List<Product> products =
                productDAO.searchAndFilter(
                        null,             // No keyword filter
                        null,             // All categories
                        null,             // Minimum price
                        null,             // Maximum price
                        null,             // Minimum rating
                        null,             // Brand
                        "ACTIVE",         // Active products only
                        "product_name",   // Sort field
                        "ASC",            // Sort direction
                        1,                // First page
                        PRODUCT_LIMIT);

        // Make products available to home.jsp.
        request.setAttribute("products", products);
        request.setAttribute("productCount", products.size());

        // Open the Home page.
        request.getRequestDispatcher("/home.jsp")
               .forward(request, response);
    }
}
