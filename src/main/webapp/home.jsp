
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.zoloto.model.User" %>
<%
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String firstName = loggedInUser.getFirstName();
    if (firstName == null || firstName.trim().isEmpty()) {
        firstName = "there";
    }

    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Zoloto | Everything You Love, All in One Place</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/home.css?v=13">
</head>
<body>

    <!-- Announcement bar -->
    <div class="top-strip">
        <span>YOUR EVERYDAY SHOPPING, MADE BETTER</span>
        <span class="strip-divider">|</span>
        <a href="<%= ctx %>/shop">EXPLORE THE LATEST DEALS ↗</a>
    </div>

    <!-- Main header -->
    <header class="main-header">
        <a href="<%= ctx %>/home" class="brand-logo">
            zoloto<span class="logo-star"></span>
        </a>

        <form class="header-search" action="<%= ctx %>/shop" method="get">
            <span class="search-icon">⌕</span>
            <input type="search" name="search"
                   placeholder="Search for products, brands and more..."
                   aria-label="Search products">
            <button type="submit">Search</button>
        </form>

        <div class="header-actions">
            <span class="hello-user">Hi, <%= firstName %></span>
            <a href="<%= ctx %>/cart" class="header-action">
                <span class="action-icon">🛒</span>
                <span>Cart</span>
            
<a href="<%=ctx%>/profile" class="header-action"> <span
					class="action-icon">👤</span> <span>Profile</span>
			</a></div>

        <button class="mobile-menu" id="mobileMenu"
                type="button" aria-label="Open navigation"
                aria-expanded="false">♙</button>
    </header>

    <!-- Category navigation -->
    <nav class="category-nav" id="categoryNav">
        <a href="<%= ctx %>/categories">All Categories</a>
        <a href="<%= ctx %>/shop">Fashion</a>
        <a href="<%= ctx %>/shop">Electronics</a>
        <a href="<%= ctx %>/shop">Home & Living</a>
        <a href="<%= ctx %>/shop">Beauty</a>
        <a href="<%= ctx %>/shop">Jewellery</a>
        <a href="<%= ctx %>/shop">Accessories</a>
        <a href="<%= ctx %>/shop" class="deals-link">Today's Deals ↗</a>
    </nav>

    <main>

        <!-- Main promotional banner -->
        <section class="hero">
            <div class="hero-copy">
                <span class="hero-label">WELCOME TO ZOLOTO</span>

                <h1>Good finds.<br>
                    <em>Great feelings.</em>
                </h1>

                <p>
                    From everyday essentials to little luxuries,
                    discover more of what you love in one place.
                </p>

                <div class="hero-buttons">
                    <a href="<%= ctx %>/shop" class="btn-primary">
                        Start shopping <span>↗</span>
                    </a>
                    <a href="<%= ctx %>/categories" class="btn-secondary">
                        Browse categories →
                    </a>
                </div>

                <div class="hero-benefits">
                    <span>✳ Discover something new</span>
                    <span>◇ Find your favourites</span>
                </div>
            </div>

            <div class="hero-art">
                <div class="hero-art-image"></div>
                <div class="hero-art-shape"></div>

                <div class="hero-floating-card">
                    <span class="floating-card-label">THE ZOLOTO EDIT</span>
                    <strong>Everyday favourites</strong>
                    <span>Explore your next find ↗</span>
                </div>

                <span class="hero-art-caption">STYLE · HOME · TECH · MORE</span>
            </div>
        </section>

        <!-- Shopping categories -->
        <section class="categories-section">
            <div class="section-heading">
                <div>
                    <span class="section-kicker">A LITTLE BIT OF EVERYTHING</span>
                    <h2>Shop by <em>category.</em></h2>
                </div>
                <a href="<%= ctx %>/categories" class="view-all">
                    All categories <span>↗</span>
                </a>
            </div>

            <div class="category-grid">

                <a href="<%= ctx %>/shop" class="category-card">
                    <div class="category-picture picture-fashion">
                        <span class="category-symbol">01</span>
                    </div>
                    <div class="category-details">
                        <div>
                            <h3>Fashion</h3>
                            <p>Wear your style</p>
                        </div>
                        <span class="card-arrow">↗</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="category-card">
                    <div class="category-picture picture-electronics">
                        <span class="category-symbol">02</span>
                    </div>
                    <div class="category-details">
                        <div>
                            <h3>Electronics</h3>
                            <p>Smart everyday living</p>
                        </div>
                        <span class="card-arrow">↗</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="category-card">
                    <div class="category-picture picture-home">
                        <span class="category-symbol">03</span>
                    </div>
                    <div class="category-details">
                        <div>
                            <h3>Home & Living</h3>
                            <p>Make yourself at home</p>
                        </div>
                        <span class="card-arrow">↗</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="category-card">
                    <div class="category-picture picture-beauty">
                        <span class="category-symbol">04</span>
                    </div>
                    <div class="category-details">
                        <div>
                            <h3>Beauty</h3>
                            <p>Your daily self-care</p>
                        </div>
                        <span class="card-arrow">↗</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="category-card">
                    <div class="category-picture picture-jewellery">
                        <span class="category-symbol">05</span>
                    </div>
                    <div class="category-details">
                        <div>
                            <h3>Jewellery</h3>
                            <p>Little things that shine</p>
                        </div>
                        <span class="card-arrow">↗</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="category-card">
                    <div class="category-picture picture-accessories">
                        <span class="category-symbol">06</span>
                    </div>
                    <div class="category-details">
                        <div>
                            <h3>Accessories</h3>
                            <p>Finish your look</p>
                        </div>
                        <span class="card-arrow">↗</span>
                    </div>
                </a>

            </div>
        </section>

        <!-- Deals banner -->
        <section class="deal-banner">
            <div class="deal-copy">
                <span class="section-kicker">A LITTLE SOMETHING EXTRA</span>
                <h2>Make room for<br><em>something lovely.</em></h2>
                <p>
                    Find your next favourite across fashion, tech,
                    home, beauty and more.
                </p>
                <a href="<%= ctx %>/shop" class="btn-primary">
                    Explore the shop <span>↗</span>
                </a>
            </div>

            <div class="deal-image">
                <span>GOOD THINGS ARE WAITING</span>
            </div>
        </section>

        <!-- Featured product display -->
        <section class="products-section">
            <div class="section-heading">
                <div>
                    <span class="section-kicker">A FEW THINGS TO LOVE</span>
                    <h2>Picked for <em>you.</em></h2>
                </div>
                <a href="<%= ctx %>/shop" class="view-all">
                    Shop all products <span>↗</span>
                </a>
            </div>

            <p class="section-description">
                A mix of everyday essentials and little upgrades worth discovering.
            </p>

            <div class="product-grid">

                <a href="<%= ctx %>/shop" class="product-card">
                    <div class="product-image product-fashion">
                        <span class="product-tag">STYLE PICK</span>
                        <span class="product-quick-view">Explore ↗</span>
                    </div>
                    <div class="product-info">
                        <span class="product-category">FASHION</span>
                        <h3>Everyday Style Essentials</h3>
                        <p class="product-subtitle">Your look, your way</p>
                        <span class="product-link">Discover more →</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="product-card">
                    <div class="product-image product-tech">
                        <span class="product-tag">TECH FINDS</span>
                        <span class="product-quick-view">Explore ↗</span>
                    </div>
                    <div class="product-info">
                        <span class="product-category">ELECTRONICS</span>
                        <h3>Everyday Tech Upgrades</h3>
                        <p class="product-subtitle">Little things that do more</p>
                        <span class="product-link">Discover more →</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="product-card">
                    <div class="product-image product-living">
                        <span class="product-tag">HOME FAVOURITE</span>
                        <span class="product-quick-view">Explore ↗</span>
                    </div>
                    <div class="product-info">
                        <span class="product-category">HOME & LIVING</span>
                        <h3>Thoughtful Home Finds</h3>
                        <p class="product-subtitle">Make your space yours</p>
                        <span class="product-link">Discover more →</span>
                    </div>
                </a>

                <a href="<%= ctx %>/shop" class="product-card">
                    <div class="product-image product-beauty">
                        <span class="product-tag">SELF-CARE</span>
                        <span class="product-quick-view">Explore ↗</span>
                    </div>
                    <div class="product-info">
                        <span class="product-category">BEAUTY</span>
                        <h3>Everyday Beauty Picks</h3>
                        <p class="product-subtitle">A moment just for you</p>
                        <span class="product-link">Discover more →</span>
                    </div>
                </a>

            </div>

            <div class="products-cta">
                <a href="<%= ctx %>/shop" class="btn-outline">
                    Explore all products <span>↗</span>
                </a>
            </div>
        </section>

        <!-- Brand promise strip -->
        <section class="service-strip">
            <div class="service-item">
                <span class="service-icon">✳</span>
                <div>
                    <h3>Plenty to discover</h3>
                    <p>Explore a variety of categories</p>
                </div>
            </div>

            <div class="service-item">
                <span class="service-icon">◇</span>
                <div>
                    <h3>One place to shop</h3>
                    <p>Find more of what you love</p>
                </div>
            </div>

            <div class="service-item">
                <span class="service-icon">♡</span>
                <div>
                    <h3>Made for your everyday</h3>
                    <p>Small finds, big happiness</p>
                </div>
            </div>
        </section>

        <!-- Brand story -->
        <section class="brand-story" id="our-story">
            <span class="section-kicker">A WORLD OF GOOD FINDS</span>
            <h2>More choice.<br><em>More to love.</em></h2>
            <p>
                Zoloto brings together the things you use, wear and enjoy.
                Explore different categories, discover something unexpected,
                and make everyday shopping feel a little more special.
            </p>
            <a href="<%= ctx %>/shop" class="underlined-link">
                Explore Zoloto ↗
            </a>
        </section>

    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-main">
            <div class="footer-brand-block">
                <a href="<%= ctx %>/home" class="brand-logo footer-logo">
                    zoloto<span class="logo-star"></span>
                </a>
                <p>Everything you love.<br>All in one place.</p>
            </div>

            <div class="footer-column">
                <h3>Explore</h3>
                <a href="<%= ctx %>/shop">Shop all</a>
                <a href="<%= ctx %>/categories">Categories</a>
                <a href="<%= ctx %>/shop">New finds</a>
            </div>

            <div class="footer-column">
                <h3>Your account</h3>
                <a href="<%= ctx %>/cart">Shopping cart</a>
                <a href="<%= ctx %>/home">My account</a>
                <a href="<%= ctx %>/logout">Log out</a>
            </div>

            <div class="footer-column">
                <h3>About Zoloto</h3>
                <a href="#our-story">Our story</a>
                <a href="<%= ctx %>/categories">Discover categories</a>
                <a href="<%= ctx %>/shop">Start shopping</a>
            </div>
        </div>

        <div class="footer-bottom">
            <span>© Zoloto. All rights reserved.</span>
            <span>Everyday finds, thoughtfully brought together.</span>
        </div>
    </footer>

    <script>
        const mobileMenu = document.getElementById("mobileMenu");
        const categoryNav = document.getElementById("categoryNav");

        mobileMenu.addEventListener("click", function () {
            const isOpen = categoryNav.classList.toggle("mobile-open");
            mobileMenu.setAttribute("aria-expanded", isOpen);
        });
    </script>

</body>
</html>
