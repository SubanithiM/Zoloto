
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.zoloto.model.Product" %>
<%@ page import="com.zoloto.model.User" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    String contextPath = request.getContextPath();

    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect(contextPath + "/login");
        return;
    }

    Product product =
            (Product) request.getAttribute("product");

    if (product == null) {
        response.sendRedirect(contextPath + "/shop");
        return;
    }

    String productName = product.getProductName();

    String brand = product.getBrand();

    if (brand == null || brand.trim().isEmpty()) {
        brand = "Zoloto";
    }

    String description = product.getDescription();

    if (description == null || description.trim().isEmpty()) {
        description =
                "A thoughtfully selected piece from Zoloto.";
    }

    BigDecimal price = product.getPrice();

    if (price == null) {
        price = BigDecimal.ZERO;
    }

    BigDecimal discount = product.getDiscount();

    boolean hasDiscount =
            discount != null
            && discount.compareTo(BigDecimal.ZERO) > 0;

    BigDecimal rating = product.getRating();

    int stockQuantity = product.getStockQuantity();

    String imageUrl = product.getImageUrl();

    String imageSrc = null;

    if (imageUrl != null && !imageUrl.trim().isEmpty()) {

        imageUrl = imageUrl.trim().replace("\\", "/");

        if (imageUrl.startsWith("http://")
                || imageUrl.startsWith("https://")) {

            imageSrc = imageUrl;

        } else {

            if (imageUrl.startsWith(contextPath + "/")) {
                imageUrl = imageUrl.substring(
                        contextPath.length() + 1);
            }

            while (imageUrl.startsWith("/")) {
                imageUrl = imageUrl.substring(1);
            }

            imageSrc = contextPath + "/" + imageUrl;
        }
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <%= escapeHtml(productName) %> | Zoloto
    </title>

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/product-details.css">
</head>

<body class="product-details-page">

    <!-- ================= NAVIGATION ================= -->
    <header class="pd-navbar">

        <div class="pd-navbar-inner">

            <a href="<%= contextPath %>/home"
               class="pd-brand-logo">

                <span class="pd-logo-icon">✦</span>

                <span class="pd-logo-name">
                    Zoloto<span class="pd-logo-dot">.</span>
                </span>
            </a>

            <nav class="pd-navigation">

                <a href="<%= contextPath %>/home">
                    Home
                </a>

                <a href="<%= contextPath %>/categories">
                    Categories
                </a>

                <a href="<%= contextPath %>/shop"
                   class="pd-active">
                    Shop
                </a>

            </nav>

            <div class="pd-user-actions">

                <a href="<%= contextPath %>/cart"
                   class="pd-cart-link"
                   aria-label="Shopping cart">
                    🛒
                </a>

                <a href="<%= contextPath %>/logout"
                   class="pd-logout-link">
                    Logout
                </a>

            </div>

        </div>

    </header>


    <!-- ================= MAIN CONTENT ================= -->
    <main class="pd-main">

        <!-- Breadcrumb navigation -->
        <div class="pd-breadcrumb">

            <a href="<%= contextPath %>/home">Home</a>

            <span>/</span>

            <a href="<%= contextPath %>/shop">Shop</a>

            <span>/</span>

            <span class="pd-breadcrumb-current">
                <%= escapeHtml(productName) %>
            </span>

        </div>


        <!-- ================= PRODUCT DETAILS ================= -->
        <section class="pd-product-layout">

            <!-- LEFT: PRODUCT IMAGE -->
            <div class="pd-gallery">

                <div class="pd-main-image">

                    <% if (imageSrc != null) { %>

                        <img
                            src="<%= escapeHtml(imageSrc) %>"
                            alt="<%= escapeHtml(productName) %>"
                            class="pd-product-image"
                            onerror="
                                this.hidden = true;
                                this.parentElement
                                    .querySelector('.pd-image-fallback')
                                    .hidden = false;
                            ">

                        <div class="pd-image-fallback" hidden>
                            <span>✦</span>
                            <p>Product image unavailable</p>
                        </div>

                    <% } else { %>

                        <div class="pd-image-fallback">
                            <span>✦</span>
                            <p>Product image coming soon</p>
                        </div>

                    <% } %>

                    <% if (hasDiscount) { %>
                        <span class="pd-image-offer">
                            ✦ Save ₹<%= discount.toPlainString() %>
                        </span>
                    <% } %>

                </div>

                <p class="pd-image-caption">
                    Thoughtfully chosen. Made for you.
                </p>

            </div>


            <!-- RIGHT: PRODUCT INFORMATION -->
            <div class="pd-product-information">

                <p class="pd-eyebrow">
                    THE ZOLOTO COLLECTION
                </p>

                <div class="pd-brand-rating">

                    <span class="pd-product-brand">
                        <%= escapeHtml(brand) %>
                    </span>

                    <span class="pd-rating">
                        ★
                        <%= rating != null
                                ? escapeHtml(rating.toPlainString())
                                : "New" %>
                    </span>

                </div>

                <h1 class="pd-product-title">
                    <%= escapeHtml(productName) %>
                </h1>

                <p class="pd-product-description">
                    <%= escapeHtml(description) %>
                </p>


                <!-- Product price -->
                <div class="pd-price-section">

                    <span class="pd-current-price">
                        ₹<%= price.toPlainString() %>
                    </span>

                    <% if (hasDiscount) { %>

                        <span class="pd-discount-badge">
                            ₹<%= discount.toPlainString() %> off
                        </span>

                    <% } %>

                </div>


                <!-- Stock information -->
                <div class="pd-stock-information">

                    <% if (stockQuantity > 0) { %>

                        <span class="pd-stock-dot"></span>

                        <span>
                            In stock — <%= stockQuantity %> available
                        </span>

                    <% } else { %>

                        <span class="pd-out-stock-dot"></span>

                        <span class="pd-out-of-stock">
                            Currently out of stock
                        </span>

                    <% } %>

                </div>


                <div class="pd-divider"></div>


                <!-- SKU and product details -->
                <div class="pd-meta">

                    <% if (product.getSku() != null
                            && !product.getSku().trim().isEmpty()) { %>

                        <p>
                            <span>Product code</span>

                            <strong>
                                <%= escapeHtml(product.getSku()) %>
                            </strong>
                        </p>

                    <% } %>

                    <p>
                        <span>Brand</span>

                        <strong>
                            <%= escapeHtml(brand) %>
                        </strong>
                    </p>

                    <p>
                        <span>Availability</span>

                        <strong>
                            <%= stockQuantity > 0
                                    ? "Available"
                                    : "Out of stock" %>
                        </strong>
                    </p>

                </div>


                <!-- ================= ADD TO CART ================= -->
                <form action="<%= contextPath %>/cart"
                      method="post"
                      class="pd-cart-form">

                    <input type="hidden"
                           name="action"
                           value="add">

                    <input type="hidden"
                           name="productId"
                           value="<%= product.getProductId() %>">

                    <label for="pd-quantity">
                        Quantity
                    </label>

                    <input
                        type="number"
                        id="pd-quantity"
                        name="quantity"
                        class="pd-quantity-input"
                        value="1"
                        min="1"
                        max="<%= Math.max(stockQuantity, 1) %>"
                        required
                        <%= stockQuantity <= 0 ? "disabled" : "" %>>

                    <button
                        type="submit"
                        class="pd-add-cart-button"
                        <%= stockQuantity <= 0 ? "disabled" : "" %>>

                        Add to Cart
                        <span>↗</span>

                    </button>

                </form>


                <!-- ================= ADD TO WISHLIST ================= -->
                <form action="<%= contextPath %>/wishlist"
                      method="post"
                      class="pd-wishlist-form">

                    <input type="hidden"
                           name="action"
                           value="add">

                    <input type="hidden"
                           name="productId"
                           value="<%= product.getProductId() %>">

                    <button type="submit"
                            class="pd-wishlist-button">

                        ♡ Add to Wishlist

                    </button>

                </form>


                <!-- Shopping benefits -->
                <div class="pd-benefits">

                    <div class="pd-benefit-item">

                        <span class="pd-benefit-icon">◇</span>

                        <div>
                            <strong>Thoughtfully selected</strong>

                            <p>Pieces worth keeping.</p>
                        </div>

                    </div>

                    <div class="pd-benefit-item">

                        <span class="pd-benefit-icon">♡</span>

                        <div>
                            <strong>Made for your moments</strong>

                            <p>From everyday to extraordinary.</p>
                        </div>

                    </div>

                </div>

            </div>

        </section>


        <!-- ================= DESCRIPTION SECTION ================= -->
        <section class="pd-description-section">

            <p class="pd-eyebrow">
                A LITTLE MORE ABOUT IT
            </p>

            <h2>Product Details</h2>

            <p class="pd-long-description">
                <%= escapeHtml(description) %>
            </p>

        </section>

    </main>


    <!-- ================= FOOTER ================= -->
    <footer class="pd-footer">

        <a href="<%= contextPath %>/home"
           class="pd-footer-logo">
            Zoloto<span>.</span>
        </a>

        <p>
            Little things. Lasting feelings.
        </p>

        <div class="pd-footer-links">

            <a href="<%= contextPath %>/home">Home</a>

            <a href="<%= contextPath %>/categories">
                Categories
            </a>

            <a href="<%= contextPath %>/shop">Shop</a>

        </div>

        <div class="pd-footer-bottom">
            © 2026 Zoloto. All rights reserved.
        </div>

    </footer>

</body>
</html>
