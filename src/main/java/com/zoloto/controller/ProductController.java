
package com.zoloto.controller;

import java.io.IOException;
import java.util.List;

import com.zoloto.dao.ProductDAO;
import com.zoloto.dao.ProductDAOImpl;
import com.zoloto.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/shop")
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // DAO used to retrieve products from the database.
    private ProductDAO productDAO;

    // Maximum number of active products to display on the Shop page.
    private static final int PRODUCT_LIMIT = 52;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Check whether the user is logged in.
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        // Read Shop filter parameters.
        String keyword = request.getParameter("keyword");
        String type = request.getParameter("type");
        String sort = request.getParameter("sort");

        Integer categoryId = null;
        String filterKeyword = keyword;

        // Category and subtype filters.
        if (type != null && !type.trim().isEmpty()) {

            switch (type.trim().toLowerCase()) {

                case "clothing":
                    categoryId = 10;
                    break;

                case "shirts":
                    categoryId = 10;
                    filterKeyword = "shirt";
                    break;

                case "trousers":
                    categoryId = 10;
                    filterKeyword = "trouser";
                    break;

                case "jeans":
                    categoryId = 10;
                    filterKeyword = "jean";
                    break;

                case "footwear":
                    categoryId = 11;
                    break;

                case "shoes":
                    categoryId = 11;
                    filterKeyword = "shoe";
                    break;

                case "sandals":
                    categoryId = 11;
                    filterKeyword = "sandal";
                    break;

                case "accessories":
                    categoryId = 12;
                    break;

                case "bags":
                    categoryId = 13;
                    break;

                case "electronics":
                    categoryId = 14;
                    break;

                case "books":
                    categoryId = 15;
                    break;

                case "home":
                case "home-essentials":
                    categoryId = 16;
                    break;

                case "beauty":
                    categoryId = 17;
                    break;

                case "sports":
                    categoryId = 18;
                    break;

                case "toys":
                    categoryId = 19;
                    break;

                case "jewellery":
                    categoryId = 20;
                    break;

                case "watches":
                    categoryId = 21;
                    break;

                case "all":
                case "all-products":
                    categoryId = null;
                    break;

                default:
                    categoryId = null;
                    break;
            }
        }

        // Preserve the user's search keyword for ordinary filters.
        // Subtype filters continue to use their subtype keyword.
        if (keyword != null
                && !keyword.trim().isEmpty()
                && type != null
                && !type.trim().isEmpty()
                && !type.equalsIgnoreCase("shirts")
                && !type.equalsIgnoreCase("trousers")
                && !type.equalsIgnoreCase("jeans")
                && !type.equalsIgnoreCase("shoes")
                && !type.equalsIgnoreCase("sandals")) {

            filterKeyword = keyword.trim();
        }

        // Default sorting: product name, A to Z.
        String sortBy = "product_name";
        String sortDirection = "ASC";

        if ("price-low".equals(sort)) {

            sortBy = "price";
            sortDirection = "ASC";

        } else if ("price-high".equals(sort)) {

            sortBy = "price";
            sortDirection = "DESC";

        } else if ("name-z".equals(sort)) {

            sortBy = "product_name";
            sortDirection = "DESC";
        }

        // Retrieve up to 100 active products matching the filters.
        List<Product> products =
                productDAO.searchAndFilter(
                        filterKeyword,
                        categoryId,
                        null,
                        null,
                        null,
                        null,
                        "ACTIVE",
                        sortBy,
                        sortDirection,
                        1,
                        PRODUCT_LIMIT);

        // Make the product list available to shop.jsp.
        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        request.setAttribute("type", type);
        request.setAttribute("sort", sort);

        // Count the products returned for the current filters.
        request.setAttribute("productCount", products.size());

        request.getRequestDispatcher("/shop.jsp")
               .forward(request, response);
    }
}
