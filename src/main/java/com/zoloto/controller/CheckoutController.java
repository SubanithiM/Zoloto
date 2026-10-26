package com.zoloto.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.zoloto.dao.AddressDAO;
import com.zoloto.dao.AddressDAOImpl;
import com.zoloto.dao.CartDAO;
import com.zoloto.dao.CartDAOImpl;
import com.zoloto.dao.CartItemDAO;
import com.zoloto.dao.CartItemDAOImpl;
import com.zoloto.dao.OrderDAO;
import com.zoloto.dao.OrderDAOImpl;
import com.zoloto.dao.OrderItemDAO;
import com.zoloto.dao.OrderItemDAOImpl;
import com.zoloto.dao.ProductDAO;
import com.zoloto.dao.ProductDAOImpl;

import com.zoloto.model.Address;
import com.zoloto.model.Cart;
import com.zoloto.model.CartItem;
import com.zoloto.model.Order;
import com.zoloto.model.OrderItem;
import com.zoloto.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;
    private AddressDAO addressDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
        productDAO = new ProductDAOImpl();
        addressDAO = new AddressDAOImpl();
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
    }

    // Display checkout page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        Cart cart = cartDAO.findByUserId(userId);

        if (cart == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<CartItem> cartItems =
                cartItemDAO.findByCartId(cart.getCartId());

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            Product product = productDAO.findById(item.getProductId());

            if (product == null) {
                request.setAttribute(
                        "checkoutError",
                        "A product in your cart is no longer available.");

                request.getRequestDispatcher("/cart.jsp")
                       .forward(request, response);
                return;
            }

            if (item.getQuantity() <= 0
                    || product.getStockQuantity() < item.getQuantity()) {

                request.setAttribute(
                        "checkoutError",
                        "Available stock has changed for "
                                + product.getProductName()
                                + ". Please update your cart.");

                request.getRequestDispatcher("/cart.jsp")
                       .forward(request, response);
                return;
            }

            BigDecimal itemTotal = product.getPrice().multiply(
                    BigDecimal.valueOf(item.getQuantity()));

            subtotal = subtotal.add(itemTotal);
        }

        BigDecimal shipping = BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal.add(shipping);

        List<Address> addresses = addressDAO.findByUserId(userId);

        if (addresses == null) {
            addresses = Collections.emptyList();
        }

        Address defaultAddress =
                addressDAO.findDefaultAddressByUserId(userId);

        request.setAttribute("cart", cart);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute(
                "totalQuantity",
                cartItemDAO.getTotalQuantity(cart.getCartId()));

        request.setAttribute("addresses", addresses);
        request.setAttribute("defaultAddress", defaultAddress);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("shipping", shipping);
        request.setAttribute("grandTotal", grandTotal);

        request.getRequestDispatcher("/checkout.jsp")
               .forward(request, response);
    }

    // Process checkout form
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        // Read the selected address and payment method.
        // The checkout form should use these exact input names.
        String addressValue = request.getParameter("deliveryAddress");
        String paymentMethod = request.getParameter("paymentMethod");

        if (addressValue == null || addressValue.trim().isEmpty()) {
            showCheckoutError(
                    request, response,
                    "Please select a delivery address.");
            return;
        }

        if (paymentMethod == null
                || !(paymentMethod.equals("UPI")
                || paymentMethod.equals("PAYTM")
                || paymentMethod.equals("COD"))) {

            showCheckoutError(
                    request, response,
                    "Please select a valid payment method.");
            return;
        }

        int addressId;

        try {
            addressId = Integer.parseInt(addressValue);
        } catch (NumberFormatException e) {
            showCheckoutError(
                    request, response,
                    "The selected delivery address is invalid.");
            return;
        }

        // Verify that the address belongs to this logged-in user.
        List<Address> addresses = addressDAO.findByUserId(userId);
        boolean addressBelongsToUser = false;

        if (addresses != null) {
            for (Address address : addresses) {
                if (address.getAddressId() == addressId) {
                    addressBelongsToUser = true;
                    break;
                }
            }
        }

        if (!addressBelongsToUser) {
            showCheckoutError(
                    request, response,
                    "Please select one of your saved addresses.");
            return;
        }

        Cart cart = cartDAO.findByUserId(userId);

        if (cart == null) {
            showCheckoutError(
                    request, response,
                    "Your cart is empty. Please add products first.");
            return;
        }

        List<CartItem> cartItems =
                cartItemDAO.findByCartId(cart.getCartId());

        if (cartItems == null || cartItems.isEmpty()) {
            showCheckoutError(
                    request, response,
                    "Your cart is empty. Please add products first.");
            return;
        }

        // Recalculate prices and stock on the server.
        // Never trust totals submitted by the browser.
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            Product product = productDAO.findById(item.getProductId());

            if (product == null) {
                showCheckoutError(
                        request, response,
                        "A product in your cart is no longer available.");
                return;
            }

            if (item.getQuantity() <= 0
                    || product.getStockQuantity() < item.getQuantity()) {

                showCheckoutError(
                        request, response,
                        "Available stock has changed for "
                                + product.getProductName()
                                + ". Please update your cart.");
                return;
            }

            subtotal = subtotal.add(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())));
        }

        BigDecimal shipping = BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal.add(shipping);

        // Create a unique order number.
        String orderNumber = "ZOL-" +
                UUID.randomUUID().toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(grandTotal);
        order.setPaymentMethod(paymentMethod);

        // No payment gateway is integrated here.
        // Therefore, online payments must not be marked as paid.
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("PENDING");

        // Save the order first.
        boolean orderCreated = orderDAO.createOrder(order);

        if (!orderCreated || order.getOrderId() <= 0) {
            showCheckoutError(
                    request, response,
                    "We could not place your order. Please try again.");
            return;
        }

        // Save every product as an order item.
        boolean allItemsSaved = true;

        for (CartItem cartItem : cartItems) {

            Product product =
                    productDAO.findById(cartItem.getProductId());

            if (product == null
                    || product.getStockQuantity() < cartItem.getQuantity()) {
                allItemsSaved = false;
                break;
            }

            BigDecimal unitPrice = product.getPrice();

            BigDecimal itemTotal = unitPrice.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = new OrderItem(
                    order.getOrderId(),
                    product.getProductId(),
                    product.getProductName(),
                    cartItem.getQuantity(),
                    unitPrice,
                    itemTotal);

            if (!orderItemDAO.createOrderItem(orderItem)) {
                allItemsSaved = false;
                break;
            }
        }

        if (!allItemsSaved) {

            // Best-effort cleanup if saving any order item fails.
            // This relies on the database allowing the order to be deleted.
            orderItemDAO.deleteByOrderId(order.getOrderId());
            orderDAO.deleteOrder(order.getOrderId());

            showCheckoutError(
                    request, response,
                    "We could not save all order items. "
                            + "Your cart has been kept. Please try again.");
            return;
        }

        // Clear the cart only after the order and all its items are saved.
        if (!cartItemDAO.clearCart(cart.getCartId())) {

            // Keep the saved order; do not delete it just because
            // cart clearing failed.
            session.setAttribute("checkoutWarning",
                    "Your order was saved, but your cart could not be "
                            + "cleared automatically.");
        }

        // Store confirmation details for the next page.
        session.setAttribute("placedOrderNumber", orderNumber);
        session.setAttribute("placedOrderTotal", grandTotal);
        session.setAttribute("placedPaymentMethod", paymentMethod);
        session.setAttribute("placedPaymentStatus", "PENDING");

        // The confirmation JSP must be created at this location.
        response.sendRedirect(
                request.getContextPath() + "/order-success.jsp");
    }

    private void showCheckoutError(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws ServletException, IOException {

        request.setAttribute("checkoutError", message);

        doGet(request, response);
    }
}
