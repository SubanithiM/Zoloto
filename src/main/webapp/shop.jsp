
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.zoloto.model.Product" %>
<%@ page import="com.zoloto.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop | Zoloto</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/shop.css">
</head>
<body>

<%
    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    List<Product> products =
            (List<Product>) request.getAttribute("products");

    String keyword = (String) request.getAttribute("keyword");
    String sort = (String) request.getAttribute("sort");
    String type = (String) request.getAttribute("type");
    String contextPath = request.getContextPath();

    String selectedCategory = "All Products";

    if ("shirts".equals(type)) selectedCategory = "Shirts";
    else if ("trousers".equals(type)) selectedCategory = "Trousers";
    else if ("jeans".equals(type)) selectedCategory = "Jeans";
    else if ("shoes".equals(type)) selectedCategory = "Shoes";
    else if ("sandals".equals(type)) selectedCategory = "Sandals";
    else if ("accessories".equals(type)) selectedCategory = "Accessories";
    else if ("bags".equals(type)) selectedCategory = "Bags";
    else if ("electronics".equals(type)) selectedCategory = "Electronics";
    else if ("books".equals(type)) selectedCategory = "Books";
    else if ("home".equals(type)) selectedCategory = "Home Essentials";
    else if ("beauty".equals(type)) selectedCategory = "Beauty";
    else if ("sports".equals(type)) selectedCategory = "Sports";
    else if ("toys".equals(type)) selectedCategory = "Toys";
    else if ("jewellery".equals(type)) selectedCategory = "Jewellery";
    else if ("watches".equals(type)) selectedCategory = "Watches";
%>

<!-- ================= NAVBAR ================= -->
<header class="navbar">
    <a href="<%= contextPath %>/home" class="brand">
        <span class="brand-icon">✦</span>
        <span class="brand-name">Zoloto<span class="brand-dot">.</span></span>
    </a>

    <nav class="nav-links">
        <a href="<%= contextPath %>/home">Home</a>
        <a href="<%= contextPath %>/categories">Categories</a>
        <a href="<%= contextPath %>/shop" class="active">Shop</a>
    </nav>

    <div class="nav-right">
        <a href="<%= contextPath %>/cart"
           class="cart-link"
           aria-label="Shopping cart">
            <span>🛒</span>
        </a>

        <span class="welcome-user">
            Hi, <%= loggedInUser.getFirstName() %>
        </span>

        <a href="<%= contextPath %>/logout" class="logout-button">
            ⇥ Logout
        </a>
    </div>
</header>

<main>

    <!-- ================= SHOP INTRO ================= -->
    <section class="shop-intro">
        <div class="intro-content">
            <p class="eyebrow">THE COLLECTION</p>
            <h1>Shop everything</h1>
            <p class="intro-description">
                Find something wonderful for yourself or someone special.
            </p>
        </div>

        <div class="intro-decoration">
            <span class="decoration-star">✦</span>
            <span class="decoration-text">Little things.<br>Big happiness.</span>
        </div>
    </section>

    <!-- ================= SEARCH ================= -->
    <section class="shop-controls">
        <form action="<%= contextPath %>/shop"
              method="get"
              class="shop-search">

            <span class="search-icon">⌕</span>

            <input type="text"
                   name="keyword"
                   placeholder="Search products, brands..."
                   value="<%= keyword != null ? keyword : "" %>">

            <% if (type != null && !type.isEmpty()) { %>
                <input type="hidden" name="type" value="<%= type %>">
            <% } %>

            <% if (sort != null && !sort.isEmpty()) { %>
                <input type="hidden" name="sort" value="<%= sort %>">
            <% } %>

            <button type="submit">Search</button>
        </form>
    </section>

    <!-- ================= CATEGORY AND SORT ================= -->
    <section class="collection-controls">
        <div class="collection-heading">
            <div>
                <p class="eyebrow">OUR PRODUCTS</p>
                <h2><%= selectedCategory %></h2>
                <p class="collection-subtitle">
                    Handpicked finds for every kind of day.
                </p>
            </div>
        </div>

        <div class="filter-controls">

            <!-- Category filter -->
            <form action="<%= contextPath %>/shop"
                  method="get"
                  class="filter-form">

                <% if (keyword != null && !keyword.trim().isEmpty()) { %>
                    <input type="hidden" name="keyword" value="<%= keyword %>">
                <% } %>

                <% if (sort != null && !sort.isEmpty()) { %>
                    <input type="hidden" name="sort" value="<%= sort %>">
                <% } %>

                <label for="type">Category</label>

                <select name="type" id="type"
                        onchange="this.form.submit()">
                    <option value=""
                        <%= type == null || type.isEmpty() ? "selected" : "" %>>
                        All Products
                    </option>
                    <option value="shirts"
                        <%= "shirts".equals(type) ? "selected" : "" %>>
                        Shirts
                    </option>
                    <option value="trousers"
                        <%= "trousers".equals(type) ? "selected" : "" %>>
                        Trousers
                    </option>
                    <option value="jeans"
                        <%= "jeans".equals(type) ? "selected" : "" %>>
                        Jeans
                    </option>
                    <option value="shoes"
                        <%= "shoes".equals(type) ? "selected" : "" %>>
                        Shoes
                    </option>
                    <option value="sandals"
                        <%= "sandals".equals(type) ? "selected" : "" %>>
                        Sandals
                    </option>
                    <option value="accessories"
                        <%= "accessories".equals(type) ? "selected" : "" %>>
                        Accessories
                    </option>
                    <option value="bags"
                        <%= "bags".equals(type) ? "selected" : "" %>>
                        Bags
                    </option>
                    <option value="electronics"
                        <%= "electronics".equals(type) ? "selected" : "" %>>
                        Electronics
                    </option>
                    <option value="books"
                        <%= "books".equals(type) ? "selected" : "" %>>
                        Books
                    </option>
                    <option value="home"
                        <%= "home".equals(type) ? "selected" : "" %>>
                        Home Essentials
                    </option>
                    <option value="beauty"
                        <%= "beauty".equals(type) ? "selected" : "" %>>
                        Beauty
                    </option>
                    <option value="sports"
                        <%= "sports".equals(type) ? "selected" : "" %>>
                        Sports
                    </option>
                    <option value="toys"
                        <%= "toys".equals(type) ? "selected" : "" %>>
                        Toys
                    </option>
                    <option value="jewellery"
                        <%= "jewellery".equals(type) ? "selected" : "" %>>
                        Jewellery
                    </option>
                    <option value="watches"
                        <%= "watches".equals(type) ? "selected" : "" %>>
                        Watches
                    </option>
                </select>
            </form>

            <!-- Sorting -->
            <form action="<%= contextPath %>/shop"
                  method="get"
                  class="sort-form">

                <% if (keyword != null && !keyword.trim().isEmpty()) { %>
                    <input type="hidden" name="keyword" value="<%= keyword %>">
                <% } %>

                <% if (type != null && !type.isEmpty()) { %>
                    <input type="hidden" name="type" value="<%= type %>">
                <% } %>

                <label for="sort">Sort by</label>

                <select name="sort" id="sort"
                        onchange="this.form.submit()">
                    <option value=""
                        <%= sort == null || sort.isEmpty() ? "selected" : "" %>>
                        Name: A to Z
                    </option>
                    <option value="name-z"
                        <%= "name-z".equals(sort) ? "selected" : "" %>>
                        Name: Z to A
                    </option>
                    <option value="price-low"
                        <%= "price-low".equals(sort) ? "selected" : "" %>>
                        Price: Low to High
                    </option>
                    <option value="price-high"
                        <%= "price-high".equals(sort) ? "selected" : "" %>>
                        Price: High to Low
                    </option>
                </select>
            </form>
        </div>
    </section>

    <!-- ================= PRODUCT GRID ================= -->
    <section class="shop-section">
        <div class="product-grid">

            <%
                if (products != null && !products.isEmpty()) {
                    for (Product product : products) {
            %>

            <article class="product-card">



<!-- Product image: image URL will be loaded from the database -->
					
<%-- Product image section --%>

<%-- Product image section: load the image path from the database --%>
<div class="product-image">

    <%
        String imageUrl = product.getImageUrl();

        // Use the known Gold Ring image if its database image path is empty.
        if ((imageUrl == null || imageUrl.trim().isEmpty())
                && product.getProductId() == 9) {
            imageUrl = "assets/images/products/gold-ring.jpg";
        }
    %>

						<% if (imageUrl != null && !imageUrl.trim().isEmpty()) { %>
    <img
        src="<%= contextPath %>/<%= imageUrl.startsWith("/") ? imageUrl.substring(1) : imageUrl %>"
        alt="<%= product.getProductName() %>"
        class="product-photo"
        onerror="console.error('Image failed:', this.src); this.style.display='none';"
    >
<% } else { %>
    <div class="image-placeholder">
        <span>✦</span>
        <p>Image coming soon</p>
    </div>
<% } %>

						

    <% if (product.getDiscount() != null
            && product.getDiscount().compareTo(BigDecimal.ZERO) > 0) { %>
        <span class="discount-badge">
            <span>✦</span> Save ₹<%= product.getDiscount() %>
        </span>
    <% } %>

</div>

					<div class="product-info">

                    <div class="product-brand-row">
                        <p class="product-brand">
                            <%= product.getBrand() != null
                                    && !product.getBrand().trim().isEmpty()
                                    ? product.getBrand()
                                    : "ZOLOTO" %>
                        </p>

                        <span class="product-rating">
                            ★
                            <%= product.getRating() != null
                                    ? product.getRating()
                                    : "New" %>
                        </span>
                    </div>

                    <h3 class="product-name">
                        <%= product.getProductName() %>
                    </h3>

                    <p class="product-description">
                        <%= product.getDescription() != null
                                && !product.getDescription().trim().isEmpty()
                                ? product.getDescription()
                                : "A lovely find, selected for you." %>
                    </p>

                    <div class="product-price-row">
                        <span class="product-price">
                            ₹<%= product.getPrice() != null
                                    ? product.getPrice() : BigDecimal.ZERO %>
                        </span>

                        <% if (product.getDiscount() != null
                                && product.getDiscount().compareTo(BigDecimal.ZERO) > 0) { %>
                            <span class="product-discount">
                                <%= product.getDiscount() %>% OFF
                            </span>
                        <% } %>
                    </div>

                    <div class="stock-row">
                        <% if (product.getStockQuantity() > 0) { %>
                            <span class="in-stock">
                                <span class="stock-dot"></span>
                                In stock
                            </span>
                        <% } else { %>
                            <span class="out-stock">
                                <span class="stock-dot"></span>
                                Out of stock
                            </span>
                        <% } %>
                    </div>

                    <!-- PRODUCT ACTIONS -->
                    <div class="product-actions">

                        <% if (product.getStockQuantity() > 0) { %>
                            <!-- ADD TO CART -->
                            <form action="<%= contextPath %>/cart" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden"
                                       name="productId"
                                       value="<%= product.getProductId() %>">

                                <button type="submit" class="add-cart-button">
                                    Add to Cart <span>↗</span>
                                </button>
                            </form>
                        <% } else { %>
                            <button type="button"
                                    class="add-cart-button disabled"
                                    disabled>
                                Currently Unavailable
                            </button>
                        <% } %>

                        <!-- ADD TO WISHLIST -->
                        <form action="<%= contextPath %>/wishlist"
                              method="post"
                              class="product-wishlist">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden"
                                   name="productId"
                                   value="<%= product.getProductId() %>">

                            <button type="submit" class="wishlist-button">
                                ♡ Add to Wishlist
                            </button>
                        </form>

                    </div>
                </div>
            </article>

            <%
                    }
                } else {
            %>

            <div class="no-products">
                <div class="empty-icon">✦</div>
                <h3>No products found</h3>
                <p>
                    Try another search or choose a different category.
                </p>
                <a href="<%= contextPath %>/shop" class="clear-filter">
                    View all products
                </a>
            </div>

            <%
                }
            %>

        </div>
    </section>

    <!-- ================= SHOP PROMISE ================= -->
    <section class="shop-promise">
        <div class="promise-item">
            <span class="promise-icon">✧</span>
            <div>
                <h3>Thoughtfully chosen</h3>
                <p>Pieces worth keeping.</p>
            </div>
        </div>

        <div class="promise-item">
            <span class="promise-icon">◇</span>
            <div>
                <h3>Made for your moments</h3>
                <p>From everyday to extraordinary.</p>
            </div>
        </div>

        <div class="promise-item">
            <span class="promise-icon">♡</span>
            <div>
                <h3>Always your style</h3>
                <p>Find the pieces that feel like you.</p>
            </div>
        </div>
    </section>

</main>

<!-- ================= FOOTER ================= -->
<footer class="site-footer">
    <div class="footer-top">
        <a href="<%= contextPath %>/home" class="brand footer-brand">
            <span class="brand-icon">✦</span>
            <span class="brand-name">Zoloto<span class="brand-dot">.</span></span>
        </a>

        <p class="footer-tagline">Little things. Lasting feelings.</p>

        <div class="footer-navigation">
            <a href="<%= contextPath %>/home">Home</a>
            <a href="<%= contextPath %>/categories">Collections</a>
            <a href="<%= contextPath %>/shop">Shop all</a>
            <a href="<%= contextPath %>/cart">Cart</a>
            <a href="<%= contextPath %>/logout">Log out</a>
        </div>
    </div>

    <div class="footer-bottom">
        <span>© 2026 Zoloto. All rights reserved.</span>
        <span>Made with care ✧</span>
    </div>
</footer>

</body>
</html>
