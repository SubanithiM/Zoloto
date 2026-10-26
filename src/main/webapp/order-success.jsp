 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    String orderNumber = (String) session.getAttribute("placedOrderNumber");
    BigDecimal orderTotal = (BigDecimal) session.getAttribute("placedOrderTotal");
    String paymentMethod = (String) session.getAttribute("placedPaymentMethod");
    String paymentStatus = (String) session.getAttribute("placedPaymentStatus");

    if (orderNumber == null || orderTotal == null) {
        response.sendRedirect(request.getContextPath() + "/checkout");
        return;
    }

    if (paymentMethod == null) {
        paymentMethod = "Not available";
    }

    if (paymentStatus == null) {
        paymentStatus = "PENDING";
    }

    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    String formattedTotal = currencyFormat.format(orderTotal);

    String contextPath = request.getContextPath();

    String displayPaymentMethod = paymentMethod;
    if ("UPI".equalsIgnoreCase(paymentMethod)) {
        displayPaymentMethod = "UPI";
    } else if ("PAYTM".equalsIgnoreCase(paymentMethod)) {
        displayPaymentMethod = "Paytm";
    } else if ("COD".equalsIgnoreCase(paymentMethod)) {
        displayPaymentMethod = "Cash on Delivery";
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmed | Zoloto</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/order-success.css?v=3">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            font-family: Arial, Helvetica, sans-serif;
            background: #090d14;
            color: #f5f7fb;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }

        .success-container {
            width: 100%;
            max-width: 620px;
            background: #111824;
            border: 1px solid #263345;
            border-radius: 22px;
            padding: 42px;
            text-align: center;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        }

        .success-icon {
            width: 82px;
            height: 82px;
            margin: 0 auto 24px;
            border-radius: 50%;
            background: rgba(65, 205, 145, 0.12);
            border: 1px solid rgba(65, 205, 145, 0.4);
            color: #41cd91;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 42px;
            font-weight: bold;
        }

        .eyebrow {
            color: #69c5ff;
            font-size: 12px;
            font-weight: bold;
            letter-spacing: 2px;
            text-transform: uppercase;
            margin-bottom: 12px;
        }

        h1 {
            font-size: clamp(27px, 5vw, 36px);
            margin-bottom: 14px;
            letter-spacing: -1px;
        }

        .subtitle {
            color: #aab5c5;
            font-size: 15px;
            line-height: 1.7;
            margin-bottom: 30px;
        }

        .order-card {
            text-align: left;
            background: #0b111b;
            border: 1px solid #273448;
            border-radius: 15px;
            padding: 22px;
            margin-bottom: 25px;
        }

        .order-number-label {
            color: #8997aa;
            font-size: 12px;
            margin-bottom: 8px;
        }

        .order-number {
            color: #69c5ff;
            font-size: 17px;
            font-weight: bold;
            overflow-wrap: anywhere;
            margin-bottom: 22px;
        }

        .detail-row {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 16px;
            padding: 14px 0;
            border-top: 1px solid #253143;
            font-size: 14px;
        }

        .detail-label {
            color: #9caabd;
        }

        .detail-value {
            text-align: right;
            font-weight: 600;
            overflow-wrap: anywhere;
        }

        .total-value {
            color: #69c5ff;
            font-size: 19px;
        }

        .status {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 20px;
            background: rgba(255, 190, 80, 0.12);
            color: #ffc65c;
            font-size: 11px;
            font-weight: bold;
            letter-spacing: 0.5px;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn {
            flex: 1;
            min-width: 180px;
            min-height: 48px;
            display: inline-flex;
            justify-content: center;
            align-items: center;
            border-radius: 10px;
            text-decoration: none;
            font-size: 14px;
            font-weight: bold;
            transition: 0.2s ease;
        }

        .btn-primary {
            background: #69c5ff;
            color: #07111c;
        }

        .btn-primary:hover {
            background: #91d5ff;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: #1a2534;
            border: 1px solid #344358;
            color: #f5f7fb;
        }

        .btn-secondary:hover {
            background: #243247;
        }

        .footer-note {
            color: #7f8da1;
            font-size: 12px;
            line-height: 1.6;
            margin-top: 24px;
        }

        @media (max-width: 480px) {
            .success-container {
                padding: 26px 18px;
            }

            .order-card {
                padding: 17px;
            }

            .actions {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>

<body>
    <main class="success-container">

        <div class="success-icon" aria-label="Success">✓</div>

        <p class="eyebrow">Zoloto · Order Update</p>

        <h1>Thank you for your order!</h1>

        <p class="subtitle">
            Your order has been created successfully.
            Keep your order number for future reference.
        </p>

        <section class="order-card">
            <p class="order-number-label">YOUR ORDER NUMBER</p>

            <p class="order-number"><%= orderNumber %></p>

            <div class="detail-row">
                <span class="detail-label">Order total</span>
                <span class="detail-value total-value"><%= formattedTotal %></span>
            </div>

            <div class="detail-row">
                <span class="detail-label">Payment method</span>
                <span class="detail-value"><%= displayPaymentMethod %></span>
            </div>

            <div class="detail-row">
                <span class="detail-label">Payment status</span>
                <span class="detail-value">
                    <span class="status"><%= paymentStatus %></span>
                </span>
            </div>

            <div class="detail-row">
                <span class="detail-label">Order status</span>
                <span class="detail-value">Pending</span>
            </div>
        </section>

        <div class="actions">
			<a class="btn btn-primary" href="<%=contextPath%>/shop">
				Continue Shopping </a> <a class="btn btn-secondary"
               href="<%= contextPath %>/home">
                Back to Home
            </a>
        </div>

        <p class="footer-note">
            For online payments, your payment status will remain pending
            until a payment gateway is integrated and the payment is verified.
        </p>

    </main>
</body>
</html>