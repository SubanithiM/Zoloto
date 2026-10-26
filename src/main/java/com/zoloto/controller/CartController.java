package com.zoloto.controller;

import java.io.IOException;
import java.util.List;

import com.zoloto.dao.CartDAO;
import com.zoloto.dao.CartDAOImpl;
import com.zoloto.dao.CartItemDAO;
import com.zoloto.dao.CartItemDAOImpl;
import com.zoloto.dao.ProductDAO;
import com.zoloto.dao.ProductDAOImpl;
import com.zoloto.model.Cart;
import com.zoloto.model.CartItem;
import com.zoloto.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;

    @Override
    public void init() {
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    // =========================
    // GET - Display Cart
    // =========================
    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        Cart cart = getOrCreateCart(userId);

        List<CartItem> cartItems = cartItemDAO.findByCartId(cart.getCartId());

        int totalQuantity = cartItemDAO.getTotalQuantity(cart.getCartId());

        request.setAttribute("cart", cart);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("totalQuantity", totalQuantity);

        request.getRequestDispatcher("/cart.jsp")
               .forward(request, response);
    }

    // =========================
    // POST - Cart Actions
    // =========================
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String action = request.getParameter("action");

        Cart cart = getOrCreateCart(userId);

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // =========================
        // ADD PRODUCT
        // =========================
        if ("add".equals(action)) {

            String productIdParam = request.getParameter("productId");

            if (productIdParam == null) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            try {

                int productId = Integer.parseInt(productIdParam);

                Product product = productDAO.findById(productId);

                // Product does not exist
                if (product == null) {
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }

                // Product is out of stock
                if (product.getStockQuantity() <= 0) {
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }

                CartItem existingItem =
                        cartItemDAO.findByCartIdAndProductId(
                                cart.getCartId(),
                                productId);

                if (existingItem != null) {

                    // Do not allow quantity above stock
                    if (existingItem.getQuantity()
                            < product.getStockQuantity()) {

                        cartItemDAO.incrementQuantity(
                                existingItem.getCartItemId(), 1);
                    }

                } else {

                    CartItem cartItem = new CartItem();

                    cartItem.setCartId(cart.getCartId());
                    cartItem.setProductId(productId);
                    cartItem.setQuantity(1);

                    cartItemDAO.addCartItem(cartItem);
                }

                cartDAO.updateCart(cart.getCartId());

            } catch (NumberFormatException e) {
                // Invalid product ID
            }

            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // =========================
        // INCREASE QUANTITY
        // =========================
        if ("increase".equals(action)) {

            updateQuantity(request, cart, true);

            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // =========================
        // DECREASE QUANTITY
        // =========================
        if ("decrease".equals(action)) {

            updateQuantity(request, cart, false);

            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // =========================
        // REMOVE ITEM
        // =========================
        if ("remove".equals(action)) {

            String cartItemIdParam =
                    request.getParameter("cartItemId");

            if (cartItemIdParam != null) {

                try {

                    int cartItemId =
                            Integer.parseInt(cartItemIdParam);

                    CartItem item =
                            cartItemDAO.findById(cartItemId);

                    // Make sure item belongs to this user's cart
                    if (item != null &&
                        item.getCartId() == cart.getCartId()) {

                        cartItemDAO.deleteById(cartItemId);

                        cartDAO.updateCart(cart.getCartId());
                    }

                } catch (NumberFormatException e) {
                    // Invalid cart item ID
                }
            }

            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // =========================
        // CLEAR CART
        // =========================
        if ("clear".equals(action)) {

            cartDAO.clearCart(cart.getCartId());

            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Unknown action
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    // =========================
    // Update Quantity
    // =========================
    private void updateQuantity(HttpServletRequest request,
                                Cart cart,
                                boolean increase) {

        String cartItemIdParam =
                request.getParameter("cartItemId");

        if (cartItemIdParam == null) {
            return;
        }

        try {

            int cartItemId =
                    Integer.parseInt(cartItemIdParam);

            CartItem item =
                    cartItemDAO.findById(cartItemId);

            // Make sure item belongs to this user's cart
            if (item == null ||
                item.getCartId() != cart.getCartId()) {
                return;
            }

            Product product =
                    productDAO.findById(item.getProductId());

            if (product == null) {
                return;
            }

            if (increase) {

                // Don't exceed available stock
                if (item.getQuantity()
                        < product.getStockQuantity()) {

                    cartItemDAO.incrementQuantity(
                            cartItemId, 1);
                }

            } else {

                // Decrease quantity
                if (item.getQuantity() > 1) {

                    cartItemDAO.decrementQuantity(
                            cartItemId, 1);

                } else {

                    // If quantity is already 1,
                    // remove the item
                    cartItemDAO.deleteById(cartItemId);
                }
            }

            cartDAO.updateCart(cart.getCartId());

        } catch (NumberFormatException e) {
            // Invalid cart item ID
        }
    }

    // =========================
    // Get Existing Cart
    // or Create New Cart
    // =========================
    private Cart getOrCreateCart(int userId) {

        Cart cart = cartDAO.findByUserId(userId);

        if (cart == null) {

            cart = new Cart();

            cart.setUserId(userId);

            boolean created = cartDAO.createCart(cart);

            if (created) {
                cart = cartDAO.findByUserId(userId);
            }
        }

        return cart;
    }
}