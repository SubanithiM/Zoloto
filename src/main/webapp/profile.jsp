
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.zoloto.model.User" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

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
    String ctx = request.getContextPath();

    User user = (User) request.getAttribute("profileUser");

    if (user == null) {
        response.sendRedirect(ctx + "/profile");
        return;
    }

    String firstName = user.getFirstName() == null
            ? "" : user.getFirstName();

    String lastName = user.getLastName() == null
            ? "" : user.getLastName();

    String fullName = (firstName + " " + lastName).trim();

    String initials = firstName.isEmpty()
            ? "Z"
            : firstName.substring(0, 1).toUpperCase();

    if (!lastName.isEmpty()) {
        initials += lastName.substring(0, 1).toUpperCase();
    }

    String joinedDate = "Not available";

    if (user.getCreatedAt() != null) {
        joinedDate = user.getCreatedAt().format(
            DateTimeFormatter.ofPattern("dd MMM yyyy")
        );
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Profile | Zoloto</title>

    <link rel="stylesheet"
          href="<%= ctx %>/assets/css/profile.css?v=1">
</head>

<body>

    <!-- ================= HEADER ================= -->
    <header class="profile-header">

        <a href="<%= ctx %>/home" class="brand">
            <span class="brand-name">zoloto<span>.</span></span>
        </a>

        <nav class="profile-nav">
            <a href="<%= ctx %>/home">Home</a>
            <a href="<%= ctx %>/shop">Shop</a>
            <a href="<%= ctx %>/cart">Cart</a>
        </nav>

        <a href="<%= ctx %>/Logout" class="logout-button"
           onclick="return confirm('Are you sure you want to log out?');">
            Logout <span aria-hidden="true">↗</span>
        </a>

    </header>

    <!-- ================= MAIN PROFILE ================= -->
    <main class="profile-page">

        <section class="profile-heading">
            <p class="eyebrow">YOUR PERSONAL SPACE</p>
            <h1>My Profile<span>.</span></h1>
            <p class="heading-description">
                Your details, all in one place.
            </p>
        </section>

        <section class="profile-layout">

            <!-- PROFILE IDENTITY CARD -->
            <aside class="profile-sidebar">

                <div class="profile-avatar">
                    <%= escapeHtml(initials) %>
                </div>

                <h2><%= escapeHtml(fullName) %></h2>

                <p class="profile-email">
                    <%= escapeHtml(user.getEmail()) %>
                </p>

                <span class="member-badge">Zoloto Member</span>

                <div class="sidebar-divider"></div>

                <a href="<%= ctx %>/home" class="sidebar-link">
                    <span>⌂</span> Back to Home
                </a>

                <a href="<%= ctx %>/cart" class="sidebar-link">
                    <span>🛒</span> My Cart
                </a>

            </aside>

            <!-- USER INFORMATION CARD -->
            <section class="profile-details-card">

                <div class="details-heading">
                    <div>
                        <p class="eyebrow">ACCOUNT INFORMATION</p>
                        <h2>Personal details</h2>
                    </div>

                    <span class="verified-badge">✓ Account</span>
                </div>

                <div class="details-grid">

                    <div class="detail-item">
                        <span class="detail-label">First name</span>
                        <p class="detail-value">
                            <%= escapeHtml(user.getFirstName()) %>
                        </p>
                    </div>

                    <div class="detail-item">
                        <span class="detail-label">Last name</span>
                        <p class="detail-value">
                            <%= escapeHtml(user.getLastName()) %>
                        </p>
                    </div>

                    <div class="detail-item full-width">
                        <span class="detail-label">Email address</span>
                        <p class="detail-value">
                            <%= escapeHtml(user.getEmail()) %>
                        </p>
                    </div>

                    <div class="detail-item">
                        <span class="detail-label">Mobile number</span>
                        <p class="detail-value">
                            <%= user.getMobile() == null
                                || user.getMobile().trim().isEmpty()
                                ? "Not provided"
                                : escapeHtml(user.getMobile()) %>
                        </p>
                    </div>

                    <div class="detail-item">
                        <span class="detail-label">Account role</span>
                        <p class="detail-value">
                            <%= user.getRole() == null
                                ? "Customer"
                                : escapeHtml(user.getRole()) %>
                        </p>
                    </div>

                    <div class="detail-item">
                        <span class="detail-label">Account status</span>
                        <p class="detail-value status-active">
                            <%= user.getStatus() == null
                                ? "Not available"
                                : escapeHtml(user.getStatus()) %>
                        </p>
                    </div>

                    <div class="detail-item">
                        <span class="detail-label">Member since</span>
                        <p class="detail-value">
                            <%= escapeHtml(joinedDate) %>
                        </p>
                    </div>

                </div>

                <div class="profile-note">
                    <span class="note-icon">✦</span>
                    <div>
                        <strong>Your Zoloto account</strong>
                        <p>
                            Your account information is displayed here
                            for your reference.
                        </p>
                    </div>
                </div>

                <div class="profile-actions">
                    <a href="<%= ctx %>/home" class="continue-button">
                        Continue shopping <span>→</span>
                    </a>

                    <a href="<%= ctx %>/Logout" class="logout-secondary"
                       onclick="return confirm('Are you sure you want to log out?');">
                        Logout
                    </a>
                </div>

            </section>

        </section>

        <footer class="profile-footer">
            <p>&copy; <%= java.time.Year.now() %> Zoloto. Crafted with care.</p>
        </footer>

    </main>

</body>
</html>
