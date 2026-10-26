<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="com.zoloto.model.CartItem" %>
<%@ page import="com.zoloto.model.Product" %>
<%@ page import="com.zoloto.model.User" %>
<%@ page import="com.zoloto.dao.ProductDAO" %>
<%@ page import="com.zoloto.dao.ProductDAOImpl" %>

<%!
    /*
     * Converts the image path stored in MySQL into a browser URL.
     * Example database path:
     * assets/images/products/Adjustable_Baseball_Cap.jpg
     */
    private String getCartImageSrc(String imageUrl, String contextPath) {

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return null;
        }

        String path = imageUrl.trim().replace('\\', '/');

        // Support a full URL if one is ever stored in the database.
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path.replace(" ", "%20");
        }

        // Remove leading slashes so that the context path is added once.
        while (path.startsWith("/")) {
            path = path.substring(1);
        }

        // Our project images are stored under this web application folder.
        if (!path.startsWith("assets/")) {
            path = "assets/images/products/" + path;
        }

        // Encode spaces in image filenames.
        path = path.replace(" ", "%20");

        return contextPath + "/" + path;
    }
%>

<%
    // ================= LOGIN CHECK =================
    if (session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String contextPath = request.getContextPath();

    // ================= CART DATA =================
    List<CartItem> cartItems =
            (List<CartItem>) request.getAttribute("cartItems");

    Integer totalQuantity =
            (Integer) request.getAttribute("totalQuantity");

    if (cartItems == null) {
        cartItems = Collections.emptyList();
    }

    if (totalQuantity == null) {
        totalQuantity = 0;

        for (CartItem item : cartItems) {
            totalQuantity += item.getQuantity();
        }
    }

    ProductDAO productDAO = new ProductDAOImpl();
    BigDecimal grandTotal = BigDecimal.ZERO;
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Cart | Zoloto</title>

    <!-- Cart stylesheet -->
    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/cart.css?v=7">
</head>

<body>

    <!-- ================= NAVBAR ================= -->
    <header class="navbar">

        <a href="<%= contextPath %>/home" class="logo">
            <span class="logo-icon">Z</span>
            <span class="logo-text">
                Zoloto<span class="logo-dot">.</span>
            </span>
        </a>

        <nav class="nav-links">
            <a href="<%= contextPath %>/home">Home</a>
            <a href="<%= contextPath %>/categories">Categories</a>
            <a href="<%= contextPath %>/shop">Shop</a>
        </nav>

        <div class="navbar-right">

            <a href="<%= contextPath %>/shop"
               class="nav-icon"
               title="Search products">⌕</a>

            <a href="<%= contextPath %>/wishlist"
               class="nav-icon"
               title="Wishlist">♡</a>

            <a href="<%= contextPath %>/cart"
               class="nav-icon active"
               title="Cart">🛒</a>

            <span class="nav-divider"></span>

            <span class="user-name">
                Hi,
                <%= session.getAttribute("loggedInUser") instanceof User
                    ? ((User) session.getAttribute("loggedInUser")).getFirstName()
                    : "there" %>
            </span>

            <a href="<%= contextPath %>/logout"
               class="logout-button">↗ Logout</a>

        </div>
    </header>


    <!-- ================= MAIN CART PAGE ================= -->
    <main class="container">

        <section class="page-heading">
            <p class="eyebrow">YOUR COLLECTION</p>

            <h1>
                My Cart<span class="heading-dot">.</span>
            </h1>

            <p class="heading-description">
                A few lovely finds, all in one place.
            </p>
        </section>


        <!-- ================= EMPTY CART ================= -->
        <% if (cartItems.isEmpty()) { %>

            <section class="empty-cart">

                <div class="empty-icon">♡</div>

                <p class="eyebrow">
                    A LITTLE SPACE FOR SOMETHING LOVELY
                </p>

                <h2>Your cart is waiting</h2>

                <p>
                    You haven't added any products yet.
                    Explore the collection and find something you love.
                </p>

                <a href="<%= contextPath %>/shop"
                   class="primary-button">
                    Explore the Shop <span>→</span>
                </a>

            </section>

        <% } else { %>


            <!-- ================= CART TOOLBAR ================= -->
            <div class="cart-toolbar">

                <div>
                    <h2>Your selected pieces</h2>

                    <p class="cart-count">
                        <%= totalQuantity %> item(s) in your cart
                    </p>
                </div>

                <form action="<%= contextPath %>/cart"
                      method="post"
                      onsubmit="return confirm('Are you sure you want to clear your cart?');">

                    <input type="hidden" name="action" value="clear">

                    <button type="submit" class="clear-button">
                        Clear Cart
                    </button>

                </form>

            </div>


            <!-- ================= CART LAYOUT ================= -->
            <div class="cart-layout">

                <!-- ================= CART ITEMS ================= -->
                <section class="cart-items">

                    <% for (CartItem item : cartItems) {

                        Product product =
                                productDAO.findById(item.getProductId());

                        if (product == null) {
                            continue;
                        }

                        BigDecimal itemTotal = product.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity())
                        );

                        grandTotal = grandTotal.add(itemTotal);

                        // Keep the existing custom display name for the ring.
                        boolean isRing = product.getProductId() == 9;

                        String displayName = isRing
                                ? "Asthetic Gold Ring for Women"
                                : product.getProductName();

                        /*
                         * IMPORTANT:
                         * Use the real image path supplied by ProductDAOImpl.
                         * Do not hard-code a particular product ID or filename.
                         */
                        String imageSrc =
                                getCartImageSrc(product.getImageUrl(), contextPath);
                    %>

                    <article class="cart-item">

                        <!-- ================= PRODUCT IMAGE ================= -->
                        <div class="cart-product-image">

                            <!-- Show a placeholder when the database has no image path. -->
                            <div class="image-placeholder"
                                 style="<%= imageSrc == null
                                        ? "display: flex;"
                                        : "display: none;" %>">
                                <span>Z</span>
                            </div>

                            <!-- Load each product's own database image. -->
                            <% if (imageSrc != null) { %>

                                <img
                                    src="<%= imageSrc %>"
                                    alt="<%= displayName %>"
                                    loading="lazy"
                                    onerror="this.style.display='none';
                                             this.previousElementSibling.style.display='flex';">

                            <% } %>

                        </div>


                        <!-- ================= PRODUCT INFORMATION ================= -->
                        <div class="product-info">

                            <span class="product-brand">Zoloto</span>

                            <h2 class="product-name">
                                <%= displayName == null ? "" : displayName %>
                            </h2>

                            <p class="product-description">
                                <% if (isRing) { %>
                                    Elegant gold ring for women, designed
                                    for timeless beauty.
                                <% } else { %>
                                    <%= product.getDescription() == null
                                            ? ""
                                            : product.getDescription() %>
                                <% } %>
                            </p>

                            <div class="product-price">
                                ₹<%= product.getPrice().setScale(
                                        2, RoundingMode.HALF_UP) %>
                            </div>

                        </div>


                        <!-- ================= QUANTITY CONTROLS ================= -->
                        <div class="quantity-controls">

                            <form action="<%= contextPath %>/cart"
                                  method="post">

                                <input type="hidden"
                                       name="action"
                                       value="decrease">

                                <input type="hidden"
                                       name="cartItemId"
                                       value="<%= item.getCartItemId() %>">

                                <button type="submit"
                                        class="quantity-button"
                                        aria-label="Decrease quantity">
                                    −
                                </button>

                            </form>

                            <span class="quantity">
                                <%= item.getQuantity() %>
                            </span>

                            <form action="<%= contextPath %>/cart"
                                  method="post">

                                <input type="hidden"
                                       name="action"
                                       value="increase">

                                <input type="hidden"
                                       name="cartItemId"
                                       value="<%= item.getCartItemId() %>">

                                <button type="submit"
                                        class="quantity-button"
                                        aria-label="Increase quantity">
                                    +
                                </button>

                            </form>

                        </div>


                        <!-- ================= ITEM TOTAL AND REMOVE ================= -->
                        <div class="item-actions">

                            <strong class="item-total">
                                ₹<%= itemTotal.setScale(
                                        2, RoundingMode.HALF_UP) %>
                            </strong>

                            <form action="<%= contextPath %>/cart"
                                  method="post"
                                  onsubmit="return confirm('Remove this item from your cart?');">

                                <input type="hidden"
                                       name="action"
                                       value="remove">

                                <input type="hidden"
                                       name="cartItemId"
                                       value="<%= item.getCartItemId() %>">

                                <button type="submit" class="remove-button">
                                    Remove
                                </button>

                            </form>

                        </div>

                    </article>

                    <% } %>


                    <!-- ================= CONTINUE SHOPPING ================= -->
                    <a href="<%= contextPath %>/shop"
                       class="continue-shopping">
                        <span>←</span> Continue Shopping
                    </a>

                </section>


                <!-- ================= ORDER SUMMARY ================= -->
                <aside class="order-summary">

                    <p class="eyebrow">YOUR ORDER</p>

                    <h2>Order Summary</h2>

                    <div class="summary-row">
                        <span>Items</span>
                        <span><%= totalQuantity %></span>
                    </div>

                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span>
                            ₹<%= grandTotal.setScale(
                                    2, RoundingMode.HALF_UP) %>
                        </span>
                    </div>

                    <div class="summary-row">
                        <span>Shipping</span>
                        <span class="free-shipping">Free</span>
                    </div>

                    <div class="summary-row total-row">
                        <span>Total</span>
                        <span>
                            ₹<%= grandTotal.setScale(
                                    2, RoundingMode.HALF_UP) %>
                        </span>
                    </div>

                    <p class="summary-note">
                        A little happiness, carefully collected.
                    </p>

                    <a href="<%= contextPath %>/checkout"
                       class="checkout-button">
                        Proceed to Checkout <span>→</span>
                    </a>

                    <div class="secure-note">
                        <span>♡</span>
                        Your shopping journey, made simple
                    </div>

                </aside>

            </div>

        <% } %>

    </main>


    <!-- ================= FOOTER ================= -->
    <footer class="footer">

        <a href="<%= contextPath %>/home" class="footer-logo">
            Zoloto<span>.</span>
        </a>

        <p>Little things. Big happiness.</p>

        <p class="copyright">
            © 2026 Zoloto. All Rights Reserved.
        </p>

    </footer>

</body>
</html>