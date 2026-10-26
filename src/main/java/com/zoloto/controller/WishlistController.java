
package com.zoloto.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.zoloto.dao.ProductDAO;
import com.zoloto.dao.ProductDAOImpl;
import com.zoloto.dao.WishlistDAO;
import com.zoloto.dao.WishlistDAOImpl;
import com.zoloto.model.Product;
import com.zoloto.model.User;
import com.zoloto.model.Wishlist;

@WebServlet("/wishlist")
public class WishlistController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private WishlistDAO wishlistDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        wishlistDAO = new WishlistDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    // Get the logged-in user's ID using the existing session.
    private Integer getLoggedInUserId(HttpSession session) {

        if (session == null) {
            return null;
        }

        Object userIdObject = session.getAttribute("userId");

        if (userIdObject instanceof Number) {
            int userId = ((Number) userIdObject).intValue();

            if (userId > 0) {
                return userId;
            }
        }

        Object loggedInUserObject =
                session.getAttribute("loggedInUser");

        if (loggedInUserObject instanceof User) {
            User user = (User) loggedInUserObject;
            int userId = user.getUserId();

            if (userId > 0) {
                return userId;
            }
        }

        return null;
    }

    // =====================================================
    // GET /wishlist
    // =====================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer userId = getLoggedInUserId(session);

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        try {
            List<Wishlist> entries =
                    wishlistDAO.findByUserId(userId);

            if (entries == null) {
                entries = new ArrayList<>();
            }

            List<Product> products = new ArrayList<>();

            for (Wishlist entry : entries) {
                if (entry == null) {
                    continue;
                }

                Product product =
                        productDAO.findById(entry.getProductId());

                if (product != null) {
                    products.add(product);
                }
            }

            request.setAttribute("wishlistEntries", entries);
            request.setAttribute("wishlistProducts", products);
            request.setAttribute("wishlistItems", products);
            request.setAttribute("wishlistCount", products.size());

            String status = request.getParameter("status");

            if ("added".equals(status)) {
                request.setAttribute(
                        "wishlistMessage",
                        "Product added to your wishlist.");
            } else if ("alreadyAdded".equals(status)) {
                request.setAttribute(
                        "wishlistMessage",
                        "This product is already in your wishlist.");
            } else if ("removed".equals(status)) {
                request.setAttribute(
                        "wishlistMessage",
                        "Product removed from your wishlist.");
            } else if ("cleared".equals(status)) {
                request.setAttribute(
                        "wishlistMessage",
                        "Your wishlist has been cleared.");
            } else if ("error".equals(status)) {
                request.setAttribute(
                        "wishlistError",
                        "The wishlist action failed. Please try again.");
            }

            request.getRequestDispatcher("/wishlist.jsp")
                   .forward(request, response);

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to load the wishlist.", e);
        }
    }

    // =====================================================
    // POST /wishlist
    // Actions: add, remove, clear
    // =====================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer userId = getLoggedInUserId(session);

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {

            int productId;

            try {
                productId = Integer.parseInt(
                        request.getParameter("productId"));
            } catch (NumberFormatException
                    | NullPointerException e) {

                redirectWithStatus(
                        request, response, "error");
                return;
            }

            if (productId <= 0) {
                redirectWithStatus(
                        request, response, "error");
                return;
            }

            // Verify that the requested product exists.
            Product product = productDAO.findById(productId);

            if (product == null) {
                redirectWithStatus(
                        request, response, "error");
                return;
            }

            // Avoid duplicate entries.
            if (wishlistDAO.exists(userId, productId)) {
                redirectWithStatus(
                        request, response, "alreadyAdded");
                return;
            }

            Wishlist wishlist = new Wishlist();
            wishlist.setUserId(userId);
            wishlist.setProductId(productId);

            boolean added = wishlistDAO.addToWishlist(wishlist);

            redirectWithStatus(
                    request, response, added ? "added" : "error");
            return;
        }

        if ("remove".equals(action)) {

            int productId;

            try {
                productId = Integer.parseInt(
                        request.getParameter("productId"));
            } catch (NumberFormatException
                    | NullPointerException e) {

                redirectWithStatus(
                        request, response, "error");
                return;
            }

            if (productId <= 0) {
                redirectWithStatus(
                        request, response, "error");
                return;
            }

            boolean removed =
                    wishlistDAO.removeByUserIdAndProductId(
                            userId, productId);

            redirectWithStatus(
                    request, response, removed ? "removed" : "error");
            return;
        }

        if ("clear".equals(action)) {

            boolean cleared =
                    wishlistDAO.removeAllByUserId(userId);

            redirectWithStatus(
                    request, response, cleared ? "cleared" : "error");
            return;
        }

        redirectWithStatus(request, response, "error");
    }

    private void redirectWithStatus(
            HttpServletRequest request,
            HttpServletResponse response,
            String status)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/wishlist?status="
                + status);
    }
}
