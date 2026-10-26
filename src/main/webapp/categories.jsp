
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.zoloto.model.Category" %>
<%@ page import="com.zoloto.model.User" %>

<%!
    // Escape database values before displaying them in HTML.
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

    // Normalize image paths saved in MySQL.
    private String normalizeCategoryImagePath(String imageUrl) {
        if (imageUrl == null) {
            return "";
        }

        String path = imageUrl.trim().replace('\\', '/');

        if (path.isEmpty() || path.contains("..")) {
            return "";
        }

        // Keep valid external URLs unchanged.
        if (path.startsWith("https://") || path.startsWith("http://")) {
            return path;
        }

        // Remove leading slashes from relative paths.
        while (path.startsWith("/")) {
            path = path.substring(1);
        }

        return path;
    }
%>

<%
    // ================= USER SESSION =================
    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String contextPath = request.getContextPath();

    String firstName = loggedInUser.getFirstName();

    if (firstName == null || firstName.trim().isEmpty()) {
        firstName = "User";
    }

    // The controller must load categories using CategoryDAO.
    List<Category> categories =
            (List<Category>) request.getAttribute("categories");

    if (categories == null) {
        categories = new ArrayList<Category>();
    }

    String keyword = request.getParameter("keyword");

    if (keyword == null) {
        keyword = "";
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Categories | Zoloto</title>

    <!-- Existing Zoloto category stylesheet -->
    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/categories.css">

    <style>
        /* ================= CATEGORY HEADING ================= */

        .category-heading {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            gap: 24px;
            margin-bottom: 25px;
            padding-bottom: 22px;
            border-bottom: 1px solid #e3d7f8;
        }

        .category-heading-content {
            flex: 1;
            min-width: 0;
        }

        .category-heading .eyebrow {
            margin-bottom: 10px;
        }

        /* ================= CATEGORY FILTERS ================= */

        .category-filters {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            gap: 12px;
            flex-shrink: 0;
        }

        .category-filters label {
            color: #756b91;
            font-size: 14px;
            font-weight: 500;
            white-space: nowrap;
        }

        .category-filters select {
            width: 190px;
            height: 56px;
            padding: 0 16px;
            border: 1px solid #e0d2ff;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.85);
            color: #51446f;
            font-family: inherit;
            font-size: 14px;
            outline: none;
            cursor: pointer;
        }

        .category-filters select:focus {
            border-color: #7951ff;
            box-shadow: 0 0 0 3px rgba(105, 65, 255, 0.08);
        }

        /* ================= CATEGORY EMPTY STATE ================= */

        .category-card[hidden],
        .category-empty[hidden] {
            display: none !important;
        }

        .category-empty {
            grid-column: 1 / -1;
            padding: 35px 20px;
            text-align: center;
            color: #827798;
            font-size: 15px;
        }

        /* ================= CATEGORY IMAGE AREA ================= */

        .category-grid .category-card .category-visual {
            position: relative !important;
            display: flex !important;
            align-items: center !important;
            justify-content: center !important;
            width: 100% !important;
            height: 200px !important;
            min-height: 200px !important;
            overflow: hidden !important;
            box-sizing: border-box;
            isolation: isolate;
        }

        /* Display the database image inside the category image tile. */

        .category-grid .category-card .category-visual-image {
            position: absolute !important;
            inset: 0 !important;

            display: block !important;

            width: 100% !important;
            height: 100% !important;

            min-width: 0 !important;
            min-height: 0 !important;

            max-width: none !important;
            max-height: none !important;

            object-fit: cover !important;
            object-position: center center !important;

            margin: 0 !important;
            padding: 0 !important;
            border: none !important;
            border-radius: inherit !important;

            z-index: 1;
        }

        /*
         * Remove decorative overlays and watermark-style text
         * added by CSS when a real image is present.
         */

        .category-grid .category-card .category-visual.has-image::before,
        .category-grid .category-card .category-visual.has-image::after {
            content: none !important;
            display: none !important;
            background: none !important;
        }

        /* Hide the decorative symbol when the image loads. */

        .category-grid .category-card .category-visual.has-image .category-symbol {
            display: none !important;
        }

        /* Keep the decorative symbol visible when no image is available. */

        .category-grid .category-card .category-symbol {
            position: relative;
            z-index: 2;
        }

        /* ================= RESPONSIVE LAYOUT ================= */

        @media (max-width: 1100px) {
            .category-heading {
                align-items: flex-start;
                flex-direction: column;
                gap: 18px;
            }

            .category-filters {
                width: 100%;
                flex-wrap: wrap;
                justify-content: flex-start;
            }
        }

        @media (max-width: 560px) {
            .category-filters {
                display: grid;
                grid-template-columns: auto minmax(0, 1fr);
                gap: 10px;
            }

            .category-filters select {
                width: 100%;
                height: 48px;
                padding: 0 10px;
                font-size: 13px;
            }

            .category-grid .category-card .category-visual {
                height: 180px !important;
                min-height: 180px !important;
            }
        }
    </style>
</head>

<body>

    <!-- ================= NAVIGATION BAR ================= -->

    <header class="navbar">

        <a href="<%= contextPath %>/home" class="logo">
            <span class="logo-icon">✦</span>
            <span>Zoloto<span class="logo-dot">.</span></span>
        </a>

        <nav class="nav-links">
            <a href="<%= contextPath %>/home">Home</a>

            <a href="<%= contextPath %>/categories" class="active">
                Categories
            </a>

            <a href="<%= contextPath %>/shop">Shop</a>
        </nav>

        <div class="navbar-right">

            <a href="<%= contextPath %>/cart"
               class="cart-link"
               aria-label="Shopping cart">
                🛒
            </a>

            <span class="user-name">
                Hi, <%= escapeHtml(firstName) %>
            </span>

            <a href="<%= contextPath %>/logout" class="logout-link">
                ⇥ Logout
            </a>

        </div>

    </header>

    <!-- ================= END NAVIGATION BAR ================= -->


    <main class="categories-page">

        <!-- ================= HERO SECTION ================= -->

        <section class="categories-hero">

            <div class="hero-content">

                <p class="eyebrow">THE COLLECTION</p>

                <h1>
                    Explore <span>categories</span>
                </h1>

                <p class="hero-description">
                    Find something wonderful for yourself or someone special.
                </p>

            </div>

            <div class="hero-note">

                <span class="note-star">✦</span>

                <p>
                    Little things.<br>
                    Big happiness.
                </p>

            </div>

        </section>

        <!-- ================= END HERO SECTION ================= -->


        <!-- ================= CATEGORY SEARCH ================= -->

        <section class="category-search-section">

            <form class="category-search"
                  id="categorySearchForm"
                  action="<%= contextPath %>/categories"
                  method="get"
                  autocomplete="off">

                <span class="search-icon" aria-hidden="true">⌕</span>

                <input
                    type="search"
                    id="categorySearchInput"
                    name="keyword"
                    value="<%= escapeHtml(keyword) %>"
                    placeholder="Search categories..."
                    aria-label="Search categories">

                <button type="submit">
                    Search
                </button>

            </form>

        </section>

        <!-- ================= END CATEGORY SEARCH ================= -->


        <!-- ================= CATEGORY HEADING AND FILTERS ================= -->

        <section class="category-heading">

            <div class="category-heading-content">

                <p class="eyebrow">
                    FIND YOUR FAVOURITES
                </p>

                <h2>
                    All Categories
                </h2>

                <p class="heading-description">
                    Explore collections made for every kind of day.
                </p>

            </div>


            <div class="category-filters">

                <label for="categoryFilter">
                    Category
                </label>

                <select id="categoryFilter">

                    <option value="all">
                        All Categories
                    </option>

                    <% for (Category category : categories) {
                           if (category == null ||
                               category.getCategoryName() == null) {
                               continue;
                           }
                    %>

                        <option value="<%= category.getCategoryId() %>">
                            <%= escapeHtml(category.getCategoryName()) %>
                        </option>

                    <% } %>

                </select>


                <label for="categorySort">
                    Sort by
                </label>

                <select id="categorySort">

                    <option value="name-asc">
                        Name: A to Z
                    </option>

                    <option value="name-desc">
                        Name: Z to A
                    </option>

                </select>

            </div>

        </section>

        <!-- ================= END HEADING AND FILTERS ================= -->


        <!-- ================= CATEGORY CARDS ================= -->

        <section class="category-grid" id="categoryGrid">

            <% if (categories.isEmpty()) { %>

                <div class="category-empty">
                    No categories are available right now.
                </div>

            <% } else {

                   for (Category category : categories) {

                       if (category == null ||
                           category.getCategoryName() == null) {
                           continue;
                       }

                       String categoryName = category.getCategoryName();

                       String description = category.getDescription();

                       if (description == null ||
                           description.trim().isEmpty()) {
                           description =
                                   "Explore products in this category.";
                       }

                       // Choose a decorative fallback symbol and colour.
                       String categoryLower =
                               categoryName.toLowerCase(Locale.ROOT);

                       String symbol = "✦";
                       String visualClass = "visual-lavender";

                       if (categoryLower.contains("fashion") ||
                           categoryLower.contains("clothing")) {

                           symbol = "✧";
                           visualClass = "visual-blue";

                       } else if (categoryLower.contains("beauty")) {

                           symbol = "✿";
                           visualClass = "visual-coral";

                       } else if (categoryLower.contains("accessor")) {

                           symbol = "◇";
                           visualClass = "visual-mint";

                       } else if (categoryLower.contains("electronic")) {

                           symbol = "⌁";
                           visualClass = "visual-blue";

                       } else if (categoryLower.contains("jewel")) {

                           symbol = "✧";
                           visualClass = "visual-lavender";

                       } else if (categoryLower.contains("home")) {

                           symbol = "⌂";
                           visualClass = "visual-mint";

                       } else if (categoryLower.contains("sport")) {

                           symbol = "↗";
                           visualClass = "visual-blue";

                       } else if (categoryLower.contains("toy")) {

                           symbol = "☆";
                           visualClass = "visual-coral";
                       }

                       /*
                        * Read the image path from Category.imageUrl.
                        * CategoryDAOImpl must populate this field.
                        */

                       String imagePath =
                               normalizeCategoryImagePath(
                                       category.getImageUrl()
                               );

                       String imageSrc = "";

                       if (!imagePath.isEmpty()) {

                           if (imagePath.startsWith("http://") ||
                               imagePath.startsWith("https://")) {

                               imageSrc = imagePath;

                           } else {

                               imageSrc =
                                       contextPath + "/" + imagePath;
                           }
                       }

            %>

                <article
                    class="category-card"
                    data-category-id="<%= category.getCategoryId() %>"
                    data-category-name="<%= escapeHtml(categoryName) %>"
                    data-category-description="<%= escapeHtml(description) %>">

                    <a
                        class="category-card-link"
                        href="<%= contextPath %>/shop?categoryId=<%= category.getCategoryId() %>"
                        aria-label="Explore products in <%= escapeHtml(categoryName) %>">


                        <!-- ================= CATEGORY IMAGE ================= -->

                        <div class="category-visual <%= visualClass %><%= imageSrc.isEmpty() ? "" : " has-image" %>">

                            <% if (!imageSrc.isEmpty()) { %>

                                <img
                                    class="category-visual-image"
                                    src="<%= escapeHtml(imageSrc) %>"
                                    alt="<%= escapeHtml(categoryName) %> category"
                                    loading="lazy"
                                    onerror="this.style.display='none'; this.parentElement.classList.remove('has-image');">

                            <% } %>

                            <div class="category-symbol">
                                <%= symbol %>
                            </div>

                        </div>

                        <!-- ================= END CATEGORY IMAGE ================= -->


                        <!-- ================= CATEGORY DETAILS ================= -->

                        <div class="category-info">

                            <h3>
                                <%= escapeHtml(categoryName) %>
                            </h3>

                            <span class="category-arrow">
                                ↗
                            </span>

                        </div>

                        <p class="category-caption">
                            <%= escapeHtml(description) %>
                        </p>

                        <!-- ================= END CATEGORY DETAILS ================= -->

                    </a>

                </article>

            <%
                   }
               }
            %>

            <div class="category-empty"
                 id="noMatchingCategories"
                 hidden>
                No matching categories found.
            </div>

        </section>

        <!-- ================= END CATEGORY CARDS ================= -->

    </main>


    <!-- ================= SEARCH, FILTER AND SORT ================= -->

    <script>
    document.addEventListener("DOMContentLoaded", function () {

        const searchInput =
            document.getElementById("categorySearchInput");

        const searchForm =
            document.getElementById("categorySearchForm");

        const categoryFilter =
            document.getElementById("categoryFilter");

        const categorySort =
            document.getElementById("categorySort");

        const categoryGrid =
            document.getElementById("categoryGrid");

        const noResults =
            document.getElementById("noMatchingCategories");


        function getCards() {
            return Array.from(
                categoryGrid.querySelectorAll(".category-card")
            );
        }


        function applyCategoryFilters() {

            const keyword =
                searchInput.value.trim().toLowerCase();

            const selectedId =
                categoryFilter.value;

            const sortOrder =
                categorySort.value;

            const cards = getCards();


            // Sort category cards by their displayed category names.
            cards.sort(function (a, b) {

                const nameA =
                    (a.dataset.categoryName || "").toLowerCase();

                const nameB =
                    (b.dataset.categoryName || "").toLowerCase();

                return sortOrder === "name-desc"
                    ? nameB.localeCompare(nameA)
                    : nameA.localeCompare(nameB);

            });


            // Reorder the existing cards without recreating them.
            cards.forEach(function (card) {
                categoryGrid.appendChild(card);
            });


            let visibleCount = 0;

            cards.forEach(function (card) {

                const name =
                    (card.dataset.categoryName || "").toLowerCase();

                const description =
                    (card.dataset.categoryDescription || "").toLowerCase();

                const id =
                    card.dataset.categoryId;

                const matchesSearch =
                    name.includes(keyword) ||
                    description.includes(keyword);

                const matchesCategory =
                    selectedId === "all" ||
                    id === selectedId;

                const visible =
                    matchesSearch && matchesCategory;

                card.hidden = !visible;

                if (visible) {
                    visibleCount++;
                }

            });


            // Show the empty message when no categories match.
            noResults.hidden = visibleCount !== 0;

        }


        searchInput.addEventListener(
            "input",
            applyCategoryFilters
        );

        categoryFilter.addEventListener(
            "change",
            applyCategoryFilters
        );

        categorySort.addEventListener(
            "change",
            applyCategoryFilters
        );

        searchForm.addEventListener("submit", function (event) {

            event.preventDefault();
            applyCategoryFilters();

        });


        // Apply the default category order on initial page load.
        applyCategoryFilters();

    });
    </script>

    <!-- ================= END SEARCH, FILTER AND SORT ================= -->

</body>
</html>
