
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.zoloto.model.User" %>

<%
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Payment | Zoloto</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/payment.css?v=1">
</head>
<body>

    <!-- Navigation -->
    <header class="payment-header">
        <a class="brand" href="${pageContext.request.contextPath}/home">
            <span class="brand-mark">Z</span>
            <span>zoloto<span class="brand-dot">.</span></span>
        </a>

        <nav class="main-nav">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/shop">Shop</a>
            <a href="${pageContext.request.contextPath}/categories">Categories</a>
            <a href="${pageContext.request.contextPath}/cart">Cart</a>
        </nav>

        <div class="secure-label">
            <span class="secure-icon">✓</span>
            Secure Payment
        </div>
    </header>

    <!-- Main Payment Page -->
    <main class="payment-container">

        <section class="page-heading">
            <p class="eyebrow">ONE LAST STEP</p>
            <h1>Make it <span>yours.</span></h1>
            <p class="heading-description">
                Choose your preferred payment method to continue your order.
            </p>

            <a class="back-link"
               href="${pageContext.request.contextPath}/checkout">
                ← Back to checkout
            </a>
        </section>

        <div class="payment-layout">

            <!-- Payment Selection -->
            <section class="payment-card">
                <div class="card-heading">
                    <div class="heading-icon">₹</div>
                    <div>
                        <h2>Payment method</h2>
                        <p>Select how you would like to pay.</p>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/payment"
                      method="post" id="paymentForm">

                    <label class="payment-option">
                        <input type="radio" name="paymentMethod"
                               value="UPI" required>
                        <span class="method-icon upi-icon">U</span>
                        <span class="method-details">
                            <strong>UPI</strong>
                            <small>Pay using your UPI app</small>
                        </span>
                        <span class="radio-indicator"></span>
                    </label>

                    <label class="payment-option">
                        <input type="radio" name="paymentMethod"
                               value="CARD">
                        <span class="method-icon card-icon">▰</span>
                        <span class="method-details">
                            <strong>Credit / Debit Card</strong>
                            <small>Pay using your bank card</small>
                        </span>
                        <span class="radio-indicator"></span>
                    </label>

                    <label class="payment-option">
                        <input type="radio" name="paymentMethod"
                               value="COD">
                        <span class="method-icon cod-icon">₹</span>
                        <span class="method-details">
                            <strong>Cash on Delivery</strong>
                            <small>Pay when your order arrives</small>
                        </span>
                        <span class="radio-indicator"></span>
                    </label>

                    <div class="payment-notice">
                        <span>ⓘ</span>
                        <p>
                            Choose a payment method to continue.
                            Online payments require a properly configured
                            payment processor before real money can be charged.
                        </p>
                    </div>

                    <button type="submit" class="continue-button">
                        Continue to payment <span>→</span>
                    </button>

                    <p class="security-note">
                        <span>✓</span> Your payment selection is submitted securely
                        over your application's connection.
                    </p>
                </form>
            </section>

            <!-- Information Panel -->
            <aside class="payment-aside">
                <div class="aside-decoration">✦</div>
                <p class="aside-eyebrow">THE ZOLOTO EXPERIENCE</p>
                <h2>Little things.<br>Big happiness.</h2>
                <p class="aside-description">
                    Thoughtfully chosen finds, carefully packed,
                    and ready to make your day special.
                </p>

                <div class="aside-divider"></div>

                <div class="benefit">
                    <span class="benefit-icon">✓</span>
                    <div>
                        <strong>Order protection</strong>
                        <small>Review your order before confirming.</small>
                    </div>
                </div>

                <div class="benefit">
                    <span class="benefit-icon">♡</span>
                    <div>
                        <strong>Made for you</strong>
                        <small>A shopping experience with care.</small>
                    </div>
                </div>

                <a class="aside-link"
                   href="${pageContext.request.contextPath}/shop">
                    Continue exploring <span>→</span>
                </a>
            </aside>

        </div>
    </main>

    <!-- Footer -->
    <footer class="payment-footer">
        <span>ZOLOTO</span>
        <p>Shop smarter. Live better.</p>
        <small>© 2026 Zoloto. All rights reserved.</small>
    </footer>

</body>
</html>
