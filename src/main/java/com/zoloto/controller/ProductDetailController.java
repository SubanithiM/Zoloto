
package com.zoloto.controller;

import java.io.IOException;

import com.zoloto.dao.ProductDAO;
import com.zoloto.dao.ProductDAOImpl;
import com.zoloto.model.Product;
import com.zoloto.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/product-details")
public class ProductDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Only logged-in users can access the product details page.
        User loggedInUser = session == null
                ? null
                : (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        // Read the product ID from the URL.
        String productIdParameter =
                request.getParameter("productId");

        int productId;

        try {
            if (productIdParameter == null
                    || productIdParameter.trim().isEmpty()) {
                response.sendRedirect(
                        request.getContextPath() + "/shop");
                return;
            }

            productId = Integer.parseInt(
                    productIdParameter.trim());

            if (productId <= 0) {
                response.sendRedirect(
                        request.getContextPath() + "/shop");
                return;
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath() + "/shop");
            return;
        }

        // Retrieve the selected product from MySQL.
        Product product = productDAO.findById(productId);

        if (product == null
                || product.getStatus() == null
                || !"ACTIVE".equalsIgnoreCase(
                        product.getStatus())) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/shop?message=product-not-found");
            return;
        }

        // Pass the product object to the JSP.
        request.setAttribute("product", product);

        request.getRequestDispatcher(
                "/product-details.jsp").forward(
                        request, response);
    }
}
