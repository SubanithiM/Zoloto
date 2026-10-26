
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.math.BigDecimal,java.util.List,java.util.Locale,java.text.NumberFormat,com.zoloto.model.Address,com.zoloto.model.CartItem,com.zoloto.model.Product,com.zoloto.dao.ProductDAO,com.zoloto.dao.ProductDAOImpl" %>

<%!
    // Escape text before displaying it in HTML.
    public String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    // Format prices with two decimal places.
    public String formatPrice(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        NumberFormat formatter =
            NumberFormat.getNumberInstance(new Locale("en", "IN"));

        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        return formatter.format(amount);
    }
%>

<%
    // Application context path.
    String contextPath = request.getContextPath();

    // Redirect users who are not logged in.
    if (session.getAttribute("userId") == null) {
        response.sendRedirect(contextPath + "/login");
        return;
    }

    // Values prepared by CheckoutController.
    List<CartItem> cartItems =
        (List<CartItem>) request.getAttribute("cartItems");

    List<Address> addresses =
        (List<Address>) request.getAttribute("addresses");

    Address defaultAddress =
        (Address) request.getAttribute("defaultAddress");

    BigDecimal subtotal =
        (BigDecimal) request.getAttribute("subtotal");

    BigDecimal shipping =
        (BigDecimal) request.getAttribute("shipping");

    BigDecimal grandTotal =
        (BigDecimal) request.getAttribute("grandTotal");

    if (subtotal == null) {
        subtotal = BigDecimal.ZERO;
    }

    if (shipping == null) {
        shipping = BigDecimal.ZERO;
    }

    if (grandTotal == null) {
        grandTotal = subtotal.add(shipping);
    }

    if (cartItems == null) {
        cartItems = new java.util.ArrayList<CartItem>();
    }

    if (addresses == null) {
        addresses = new java.util.ArrayList<Address>();
    }

    // Use the default address when available.
    int selectedAddressId = -1;

    if (defaultAddress != null) {
        selectedAddressId = defaultAddress.getAddressId();
    } else {
        for (Address address : addresses) {
            if (address.isDefaultAddress()) {
                selectedAddressId = address.getAddressId();
                break;
            }
        }

        if (selectedAddressId == -1 && !addresses.isEmpty()) {
            selectedAddressId = addresses.get(0).getAddressId();
        }
    }

    // Total number of pieces in the cart.
    int totalQuantity = 0;

    for (CartItem item : cartItems) {
        totalQuantity += item.getQuantity();
    }

    // Used to retrieve the product information and its image.
    ProductDAO productDAO = new ProductDAOImpl();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Checkout | Zoloto</title>

    <!-- Existing checkout stylesheet -->
    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/checkout.css?v=7">
</head>

<body>

<!-- ================= HEADER ================= -->
<header class="checkout-header">

    <a href="<%= contextPath %>/home" class="brand">
        <span class="brand-mark">Z</span>

        <span class="brand-name">
            Zoloto<span>.</span>
        </span>
    </a>

    <nav class="main-nav">
        <a href="<%= contextPath %>/home">Home</a>
        <a href="<%= contextPath %>/categories">Categories</a>
        <a href="<%= contextPath %>/shop">Shop</a>
    </nav>

    <div class="header-actions">
        <a href="<%= contextPath %>/wishlist" aria-label="Wishlist">
            ♡
        </a>

        <a href="<%= contextPath %>/cart"
           class="cart-link" aria-label="Shopping cart">
            🛒
        </a>

        <a href="<%= contextPath %>/profile" class="profile-link">
            Profile
        </a>
    </div>

</header>

<!-- ================= MAIN CHECKOUT ================= -->
<main class="checkout-container">

    <!-- Page heading -->
    <section class="checkout-hero">

        <div>
            <p class="eyebrow">ALMOST YOURS</p>

            <h1 class="checkout-title">
                Checkout<span>.</span>
            </h1>

            <p class="checkout-subtitle">
                Review your details before completing your order.
            </p>
        </div>

        <a href="<%= contextPath %>/cart" class="back-to-cart">
            &larr; Back to cart
        </a>

    </section>

    <!-- Checkout progress -->
    <section class="checkout-progress">

        <div class="progress-step completed">
            <span class="step-number">&#10003;</span>
            <span>Shopping bag</span>
        </div>

        <span class="progress-line"></span>

        <div class="progress-step active">
            <span class="step-number">02</span>
            <span>Delivery</span>
        </div>

        <span class="progress-line"></span>

        <div class="progress-step">
            <span class="step-number">03</span>
            <span>Payment</span>
        </div>

    </section>

    <!-- ================= CHECKOUT FORM ================= -->
    <form id="checkoutForm"
          action="<%= contextPath %>/checkout"
          method="post">

        <div class="checkout-layout">

            <!-- ================= LEFT COLUMN ================= -->
            <div class="checkout-left">

                <!-- DELIVERY ADDRESS -->
                <section class="checkout-card">

                    <div class="card-heading">

                        <div class="heading-icon">&#8962;</div>

                        <div class="heading-text">
                            <h2>Delivery address</h2>
                            <p>Where should we deliver your order?</p>
                        </div>

                        <a href="<%= contextPath %>/address"
                           class="manage-link">
                            Manage
                        </a>

                    </div>

                    <% if (addresses.isEmpty()) { %>

                        <div class="empty-address">

                            <p>
                                You haven't added a delivery address yet.
                            </p>

                            <a href="<%= contextPath %>/address"
                               class="primary-button">
                                Add delivery address
                                <span>&#8594;</span>
                            </a>

                        </div>

                    <% } else { %>

                        <div class="address-list">

                            <% for (Address address : addresses) {

                                boolean isSelected =
                                    address.getAddressId()
                                    == selectedAddressId;
                            %>

                                <label class="address-option <%= isSelected ? "selected" : "" %>">

                                    <input
                                        type="radio"
                                        name="deliveryAddress"
                                        value="<%= address.getAddressId() %>"
                                        <%= isSelected ? "checked" : "" %>
                                        required>

                                    <span class="custom-radio"></span>

                                    <span class="address-content">

                                        <span class="address-topline">

                                            <strong>
                                                <%= escapeHtml(address.getFullName()) %>
                                            </strong>

                                            <% if (address.isDefaultAddress()) { %>
                                                <span class="default-badge">
                                                    DEFAULT
                                                </span>
                                            <% } %>

                                        </span>

                                        <span class="address-type">
                                            <%= escapeHtml(address.getAddressType()) %>
                                        </span>

                                        <span class="address-text">
                                            <%= escapeHtml(address.getAddressLine1()) %>

                                            <% if (address.getAddressLine2() != null
                                                && !address.getAddressLine2().trim().isEmpty()) { %>

                                                , <%= escapeHtml(address.getAddressLine2()) %>

                                            <% } %>
                                        </span>

                                        <span class="address-text">
                                            <%= escapeHtml(address.getCity()) %>,
                                            <%= escapeHtml(address.getState()) %>
                                            <%= escapeHtml(address.getPincode()) %>
                                        </span>

                                        <span class="address-text">
                                            <%= escapeHtml(address.getCountry()) %>
                                        </span>

                                        <span class="address-phone">
                                            &#9742;
                                            <%= escapeHtml(address.getMobile()) %>
                                        </span>

                                    </span>

                                    <span class="address-check">
                                        &#10003;
                                    </span>

                                </label>

                            <% } %>

                        </div>

                        <a href="<%= contextPath %>/address"
                           class="add-another-address">
                            <span>+</span>
                            Add or manage another address
                        </a>

                    <% } %>

                </section>

                <!-- DELIVERY METHOD -->
                <section class="checkout-card">

                    <div class="card-heading">

                        <div class="heading-icon">&#10148;</div>

                        <div class="heading-text">
                            <h2>Delivery method</h2>

                            <p>
                                Carefully packed and delivered to your door.
                            </p>
                        </div>

                    </div>

                    <div class="delivery-method">

                        <div class="delivery-symbol">
                            &#128666;
                        </div>

                        <div class="delivery-details">

                            <strong>Standard delivery</strong>

                            <p>Free shipping on this order</p>

                        </div>

                        <span class="delivery-price">FREE</span>

                    </div>

                    <p class="delivery-note">
                        Your order will be delivered to the selected address.
                    </p>

                </section>

                <!-- PAYMENT METHOD -->
                <section class="checkout-card payment-card">

                    <div class="card-heading">

                        <div class="heading-icon">&#128179;</div>

                        <div class="heading-text">
                            <h2>Payment method</h2>

                            <p>
                                Choose how you would like to pay.
                            </p>
                        </div>

                    </div>

                    <div class="payment-options">

                        <!-- UPI -->
                        <label class="payment-option selected">

                            <input
                                type="radio"
                                name="paymentMethod"
                                value="UPI"
                                checked
                                required>

                            <span class="payment-option-icon upi-icon">
                                U
                            </span>

                            <span class="payment-option-content">
                                <strong>UPI</strong>
                                <small>Pay using your UPI app</small>
                            </span>

                            <span class="payment-radio"></span>

                        </label>

                        <!-- PAYTM -->
                        <label class="payment-option">

                            <input
                                type="radio"
                                name="paymentMethod"
                                value="PAYTM">

                            <span class="payment-option-icon paytm-icon">
                                P
                            </span>

                            <span class="payment-option-content">
                                <strong>Paytm</strong>
                                <small>Pay through Paytm</small>
                            </span>

                            <span class="payment-radio"></span>

                        </label>

                        <!-- CASH ON DELIVERY -->
                        <label class="payment-option">

                            <input
                                type="radio"
                                name="paymentMethod"
                                value="COD">

                            <span class="payment-option-icon cod-icon">
                                &#128666;
                            </span>

                            <span class="payment-option-content">
                                <strong>Cash on Delivery</strong>
                                <small>Pay when your order arrives</small>
                            </span>

                            <span class="payment-radio"></span>

                        </label>

                    </div>

                    <div class="payment-info" id="paymentInfo">
                        UPI payments require a supported payment integration.
                    </div>

                </section>

            </div>

            <!-- ================= RIGHT COLUMN ================= -->
            <aside class="order-summary">

                <div class="summary-heading">

                    <div>
                        <p class="eyebrow">YOUR SELECTION</p>
                        <h2>Order summary</h2>
                    </div>

                    <span class="summary-count">
                        <%= totalQuantity %> items
                    </span>

                </div>

                <!-- PRODUCT LIST -->
                <div class="summary-products">

                    <% if (cartItems.isEmpty()) { %>

                        <div class="empty-summary">
                            <p>Your cart is empty.</p>

                            <a href="<%= contextPath %>/shop">
                                Continue shopping
                            </a>
                        </div>

                    <% } else {

                        for (CartItem item : cartItems) {

                            Product product =
                                productDAO.findById(item.getProductId());

                            if (product == null) {
                                continue;
                            }

                            BigDecimal productPrice =
                                product.getPrice() == null
                                ? BigDecimal.ZERO
                                : product.getPrice();

                            BigDecimal itemTotal =
                                productPrice.multiply(
                                    BigDecimal.valueOf(item.getQuantity())
                                );

                            /*
                             * IMPORTANT:
                             * The image URL comes from the database
                             * through ProductDAOImpl and Product.java.
                             */
                            String imageUrl = product.getImageUrl();
                            String imageSrc = null;

                            if (imageUrl != null
                                    && !imageUrl.trim().isEmpty()) {

                                imageUrl = imageUrl.trim()
                                                   .replace('\\', '/');

                                if (imageUrl.startsWith("https://")
                                        || imageUrl.startsWith("http://")) {

                                    imageSrc = imageUrl;

                                } else {

                                    while (imageUrl.startsWith("/")) {
                                        imageUrl =
                                            imageUrl.substring(1);
                                    }

                                    imageSrc =
                                        contextPath + "/" + imageUrl;
                                }
                            }
                    %>

                        <!-- One product row -->
                        <div class="summary-product">

                            <!-- PRODUCT IMAGE -->
                            <div class="product-image">

                                <% if (imageSrc != null) { %>

                                    <img
                                        src="<%= escapeHtml(imageSrc) %>"
                                        alt="<%= escapeHtml(product.getProductName()) %>"
                                        loading="lazy"
                                        onerror="
                                            this.style.display='none';
                                            this.nextElementSibling.style.display='flex';
                                        ">

                                <% } %>

                                <!-- Fallback if no image exists or loading fails -->
                                <div class="image-placeholder"
                                     style="<%= imageSrc == null ? "display:flex;" : "display:none;" %>">
                                    Z
                                </div>

                            </div>

                            <!-- PRODUCT DETAILS -->
                            <div class="product-details">

                                <p class="product-brand">
                                    <%= escapeHtml(
                                        product.getBrand() == null
                                        || product.getBrand().trim().isEmpty()
                                        ? "Zoloto"
                                        : product.getBrand()
                                    ) %>
                                </p>

                                <h3>
                                    <%= escapeHtml(product.getProductName()) %>
                                </h3>

                                <p class="product-quantity">
                                    Quantity: <%= item.getQuantity() %>
                                </p>

                            </div>

                            <!-- ITEM TOTAL -->
                            <div class="product-total">
                                &#8377;<%= formatPrice(itemTotal) %>
                            </div>

                        </div>

                    <%  }
                       }
                    %>

                </div>

                <!-- ORDER TOTALS -->
                <div class="summary-divider"></div>

                <div class="totals-row">
                    <span>Subtotal</span>
                    <strong>
                        &#8377;<%= formatPrice(subtotal) %>
                    </strong>
                </div>

                <div class="totals-row">
                    <span>Shipping</span>

                    <strong class="shipping-free">
                        <%= shipping.compareTo(BigDecimal.ZERO) == 0
                            ? "FREE"
                            : "&#8377;" + formatPrice(shipping) %>
                    </strong>
                </div>

                <div class="summary-divider"></div>

                <div class="totals-row grand-total-row">
                    <span>Total</span>

                    <strong>
                        &#8377;<%= formatPrice(grandTotal) %>
                    </strong>
                </div>

                <!-- CHECKOUT ACTION -->
                <button type="submit"
                        class="place-order-button"
                        <%= cartItems.isEmpty() || addresses.isEmpty()
                            ? "disabled" : "" %>>
                    Continue to payment
                    <span>&#8594;</span>
                </button>

                <p class="secure-checkout-note">
                    &#128274; Your order details are protected.
                </p>

            </aside>

        </div>

    </form>

    <!-- ================= FOOTER ================= -->
    <footer class="checkout-footer">

        <p>
            &copy; <%= java.time.Year.now() %>
            Zoloto. Crafted with care.
        </p>

        <a href="<%= contextPath %>/shop">
            Continue shopping
        </a>

    </footer>

</main>

<!-- ================= JAVASCRIPT ================= -->
<script>
    // Highlight the selected delivery address.
    document.querySelectorAll(
        '.address-option input'
    ).forEach(function (radio) {

        radio.addEventListener('change', function () {

            document.querySelectorAll(
                '.address-option'
            ).forEach(function (option) {
                option.classList.remove('selected');
            });

            radio.closest(
                '.address-option'
            ).classList.add('selected');

        });

    });

    // Highlight the selected payment method.
    const paymentMessages = {
        UPI: 'UPI payments require a supported payment gateway or integration.',
        PAYTM: 'Paytm payments require a supported payment integration.',
        COD: 'Pay when your order arrives, subject to COD availability.'
    };

    document.querySelectorAll(
        '.payment-option input'
    ).forEach(function (radio) {

        radio.addEventListener('change', function () {

            document.querySelectorAll(
                '.payment-option'
            ).forEach(function (option) {
                option.classList.remove('selected');
            });

            radio.closest(
                '.payment-option'
            ).classList.add('selected');

            document.getElementById(
                'paymentInfo'
            ).textContent = paymentMessages[radio.value];

        });

    });
</script>

</body>
</html>
