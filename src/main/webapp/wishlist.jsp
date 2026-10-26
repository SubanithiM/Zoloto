
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.zoloto.model.Product" %>
<%@ page import="com.zoloto.model.User" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Locale" %>

<%
    String contextPath = request.getContextPath();

    Object loggedInUserObject = session.getAttribute("loggedInUser");
    Object userIdObject = session.getAttribute("userId");

    // Match the login check used by WishlistController.
    if (loggedInUserObject == null && userIdObject == null) {
        response.sendRedirect(contextPath + "/login");
        return;
    }

    List<Product> wishlistItems =
        (List<Product>) request.getAttribute("wishlistItems");

    if (wishlistItems == null) {
        wishlistItems = Collections.emptyList();
    }

    String initial = "U";

    if (loggedInUserObject instanceof User) {
        User loggedInUser = (User) loggedInUserObject;
        String firstName = loggedInUser.getFirstName();

        if (firstName != null && !firstName.trim().isEmpty()) {
            initial = firstName.trim().substring(0, 1).toUpperCase();
        }
    }

    String successMessage =
        (String) request.getAttribute("wishlistMessage");
    String errorMessage =
        (String) request.getAttribute("wishlistError");

    // Support flash messages from older controller implementations.
    if (successMessage == null) {
        successMessage =
            (String) session.getAttribute("wishlistSuccessMessage");
    }

    if (errorMessage == null) {
        errorMessage =
            (String) session.getAttribute("wishlistErrorMessage");
    }

    session.removeAttribute("wishlistSuccessMessage");
    session.removeAttribute("wishlistErrorMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Wishlist | Zoloto</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/wishlist.css">

    <!-- Product image display rules -->
    <style>
        .wishlist-grid .wishlist-card .product-visual {
            display: block;
            position: relative;
            width: 100%;
            aspect-ratio: 1 / 1;
            min-height: 250px;
            overflow: hidden;
            isolation: isolate;
            background: #ffffff;
            border-radius: inherit;
        }

        .wishlist-grid .wishlist-card .wishlist-product-image {
            position: absolute;
            inset: 0;
            display: block;
            width: 100%;
            height: 100%;
            max-width: 100%;
            object-fit: contain;
            object-position: center;
            box-sizing: border-box;
            padding: 16px;
            background: #ffffff;
            z-index: 1;
        }

        .wishlist-grid .wishlist-card .image-fallback {
            position: absolute;
            inset: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 10px;
            padding: 20px;
            box-sizing: border-box;
            text-align: center;
            color: #5636df;
            background: linear-gradient(145deg, #eee7ff, #cbb9f5);
            z-index: 2;
        }

        .wishlist-grid .wishlist-card .image-fallback[hidden] {
            display: none !important;
        }

        .wishlist-grid .wishlist-card .image-fallback .fallback-mark {
            display: grid;
            place-items: center;
            width: 58px;
            height: 58px;
            border-radius: 18px;
            background: rgba(255, 255, 255, 0.75);
            font-size: 28px;
        }

        .wishlist-grid .wishlist-card .image-fallback strong {
            font-family: Georgia, serif;
            font-size: 26px;
            letter-spacing: 4px;
        }

        .wishlist-grid .wishlist-card .image-fallback small {
            font-size: 12px;
            letter-spacing: 1px;
        }

        .wishlist-grid .wishlist-card .product-visual:has(.wishlist-product-image:not([hidden])) {
            background: #ffffff;
        }

        @media (max-width: 600px) {
            .wishlist-grid .wishlist-card .product-visual {
                min-height: 220px;
            }

            .wishlist-grid .wishlist-card .wishlist-product-image {
                padding: 10px;
            }
        }
    </style>
</head>

<body>

    <!-- Navigation -->
    <header class="navbar">
        <a class="logo" href="<%= contextPath %>/home">
            Zoloto <span class="logo-mark"></span>
        </a>

        <nav class="nav-links" aria-label="Main navigation">
            <a href="<%= contextPath %>/home">Home</a>
            <a href="<%= contextPath %>/shop">Shop</a>
            <a href="<%= contextPath %>/categories">Categories</a>
        </nav>

        <div class="nav-right">
            <a class="cart-link" href="<%= contextPath %>/cart">
                <span aria-hidden="true">♡</span> My Cart
            </a>

            <a class="profile-link"
               href="<%= contextPath %>/profile"
               aria-label="Profile">
                <span class="profile-avatar"><%= initial %></span>
                <span>Profile</span>
            </a>
        </div>
    </header>

    <main class="wishlist-page">

        <!-- Banner -->
        <section class="wishlist-banner">
            <div class="banner-copy">
                <span class="eyebrow">THE PIECES YOU LOVE</span>
                <h1>Your Wishlist<span>.</span></h1>
                <p>
                    A little place for everything that caught your eye.
                    Save your favourites and revisit them whenever you like.
                </p>
            </div>

            <div class="banner-heart" aria-hidden="true">♡</div>
            <span class="banner-watermark" aria-hidden="true">Z</span>
        </section>

        <!-- Messages -->
        <% if (successMessage != null && !successMessage.trim().isEmpty()) { %>
            <div class="wishlist-message success-message" role="status">
                <%= successMessage %>
            </div>
        <% } %>

        <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
            <div class="wishlist-message error-message" role="alert">
                <%= errorMessage %>
            </div>
        <% } %>

        <!-- Wishlist heading -->
        <section class="wishlist-heading">
            <div>
                <span class="section-label">SAVED FAVOURITES</span>
                <h2>Things worth keeping</h2>
                <p>Your favourite pieces, all in one place.</p>
            </div>

            <span class="item-count">
                <%= wishlistItems.size() %>
                <%= wishlistItems.size() == 1 ? "item" : "items" %>
            </span>
        </section>

        <!-- Wishlist products -->
        <% if (!wishlistItems.isEmpty()) { %>

            <section class="wishlist-grid" aria-label="Saved products">

                <% for (Product product : wishlistItems) {
                    if (product == null) {
                        continue;
                    }

                    /*
                     * Resolve the database image path.
                     * Example database value:
                     * assets/images/products/Adjustable_Baseball_Cap.jpg
                     */
                    String imagePath = product.getImageUrl();
                    String imageSrc = null;

                    if (imagePath != null && !imagePath.trim().isEmpty()) {
                        imagePath = imagePath.trim().replace("\\", "/");

                        if (imagePath.startsWith("https://")
                                || imagePath.startsWith("http://")) {
                            imageSrc = imagePath;
                        } else if (imagePath.startsWith(contextPath + "/")) {
                            imageSrc = imagePath;
                        } else if (imagePath.startsWith("/")) {
                            imageSrc = contextPath + imagePath;
                        } else {
                            imageSrc = contextPath + "/" + imagePath;
                        }
                    }

                    String formattedPrice = "0.00";
                    BigDecimal productPrice = product.getPrice();

                    if (productPrice != null) {
                        formattedPrice = String.format(
                            Locale.US, "%,.2f", productPrice);
                    }
                %>

                    <article class="wishlist-card">

                        <!-- Actual product image -->
                        <a class="product-visual"
                           href="<%= contextPath %>/product-details?productId=<%= product.getProductId() %>"
                           aria-label="View <%= product.getProductName() %>">

                            <% if (imageSrc != null) { %>
                                <img
                                    class="wishlist-product-image"
                                    src="<%= imageSrc %>"
                                    alt="<%= product.getProductName() %>"
                                    loading="lazy"
                                    onerror="
                                        this.hidden = true;
                                        this.parentElement.querySelector('.image-fallback').hidden = false;
                                    ">
                            <% } %>

                            <!-- Shown only if the product image is missing or fails -->
                            <div class="image-fallback"
                                 <%= imageSrc != null ? "hidden" : "" %>>
                                <span class="fallback-mark" aria-hidden="true">✦</span>
                                <strong>ZOLOTO</strong>
                                <small>Made for your moments</small>
                            </div>

                        </a>

                        <!-- Product information -->
                        <div class="product-content">

                            <div class="product-title-row">
                                <h3>
                                    <a href="<%= contextPath %>/product-details?productId=<%= product.getProductId() %>">
                                        <%= product.getProductName() %>
                                    </a>
                                </h3>

                                <span class="saved-heart" aria-hidden="true">♥</span>
                            </div>

                            <p class="product-description">
                                <%= product.getDescription() != null
                                    && !product.getDescription().trim().isEmpty()
                                    ? product.getDescription()
                                    : "A thoughtful piece for your everyday moments." %>
                            </p>

                            <div class="product-price">
                                ₹ <%= formattedPrice %>
                            </div>

                            <a class="view-product"
                               href="<%= contextPath %>/product-details?productId=<%= product.getProductId() %>">
                                View Product
                                <span aria-hidden="true">↗</span>
                            </a>

                            <!-- Remove from Wishlist -->
                            <form class="remove-form"
                                  action="<%= contextPath %>/wishlist"
                                  method="post"
                                  onsubmit="return confirm('Remove this item from your wishlist?');">

                                <input type="hidden" name="action" value="remove">

                                <input type="hidden"
                                       name="productId"
                                       value="<%= product.getProductId() %>">

                                <button type="submit" class="remove-button">
                                    Remove from Wishlist
                                </button>
                            </form>

                        </div>
                    </article>

                <% } %>

            </section>

            <!-- Clear Wishlist -->
            <div class="wishlist-bottom-action">
                <form action="<%= contextPath %>/wishlist"
                      method="post"
                      onsubmit="return confirm('Clear all items from your wishlist?');">

                    <input type="hidden" name="action" value="clear">

                    <button type="submit" class="clear-button">
                        Clear Wishlist
                    </button>
                </form>
            </div>

        <% } else { %>

            <!-- Empty Wishlist -->
            <section class="empty-wishlist">
                <div class="empty-heart" aria-hidden="true">♡</div>

                <span class="section-label">A SPACE FOR YOUR FAVOURITES</span>

                <h2>Nothing saved just yet.</h2>

                <p>
                    When a piece feels like you, save it here.
                    Your next favourite may be waiting to be discovered.
                </p>

                <a class="browse-button" href="<%= contextPath %>/shop">
                    Explore the Shop
                    <span aria-hidden="true">↗</span>
                </a>
            </section>

        <% } %>

    </main>

    <!-- Footer -->
    <footer class="site-footer">
        <div class="footer-main">

            <div class="footer-brand">
                <a class="footer-logo" href="<%= contextPath %>/home">
                    ZOLOTO <span></span>
                </a>
                <p>Little treasures. Lasting feelings.</p>
            </div>

            <div class="footer-column">
                <h3>DISCOVER</h3>
                <a href="<%= contextPath %>/home">Home</a>
                <a href="<%= contextPath %>/shop">Shop all</a>
                <a href="<%= contextPath %>/categories">Categories</a>
            </div>

            <div class="footer-column">
                <h3>YOUR ACCOUNT</h3>
                <a href="<%= contextPath %>/profile">Profile</a>
                <a href="<%= contextPath %>/cart">My Cart</a>
                <a href="<%= contextPath %>/wishlist">Wishlist</a>
            </div>

            <div class="footer-note">
                <span>MADE FOR YOUR MOMENTS</span>
                <p>
                    Meaningful details, thoughtful gifts, and little
                    treasures to keep close.
                </p>
            </div>

        </div>

        <div class="footer-bottom">
            <span>
                © <%= java.time.Year.now().getValue() %>
                Zoloto. All rights reserved.
            </span>

            <span>Little treasures. Lasting feelings.</span>

            <a href="#"
               onclick="window.scrollTo({top:0,behavior:'smooth'}); return false;">
                Back to top ↑
            </a>
        </div>
    </footer>

</body>
</html>
