
<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Zoloto | Welcome Back</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/auth.css?v=1">

</head>
<body class="auth-body login-body">

	<div class="auth-decoration auth-decoration-one"></div>
	<div class="auth-decoration auth-decoration-two"></div>
	<div class="auth-decoration auth-decoration-three"></div>

	<main class="auth-layout">

		<!-- Left promotional panel -->
		<section class="auth-showcase login-showcase">
			<a class="showcase-brand"
				href="${pageContext.request.contextPath}/home"> <span
				class="showcase-brand-icon">Z</span> <span>zoloto<span
					class="brand-period">.</span></span>
			</a>

			<div class="showcase-copy">
				<span class="showcase-eyebrow">YOUR WORLD, YOUR STYLE</span>
				<h1>
					Good finds.<br> <em>Great feelings.</em>
				</h1>
				<p>Discover the things you love, from everyday essentials to
					little luxuries.</p>

				<div class="showcase-pills">
					<span>Fashion</span> <span>Technology</span> <span>Home
						&amp; Living</span> <span>Beauty</span>
				</div>
			</div>

			<div class="showcase-art">
				<img
					src="https://images.unsplash.com/photo-1490481651871-ab68de25d43d?auto=format&fit=crop&w=1000&q=85"
					alt="Fashion and lifestyle collection">
				<div class="showcase-art-label">
					<span class="art-label-dot"></span>
					<div>
						<strong>Your next favourite</strong> <small>is waiting for
							you</small>
					</div>
					<span class="art-label-star">✦</span>
				</div>
			</div>

			<div class="showcase-bottom">
				<span>SHOP WHAT YOU LOVE</span> <span>01 — ZOLOTO</span>
			</div>
		</section>

		<!-- Login form panel -->
		<section class="auth-form-panel">
			<a class="mobile-brand"
				href="${pageContext.request.contextPath}/home"> <span
				class="showcase-brand-icon">Z</span> <span>zoloto<span
					class="brand-period">.</span></span>
			</a>

			<div class="auth-form-content">
				<div class="auth-top-note">
					<span class="auth-note-icon">✦</span> YOUR SHOPPING JOURNEY
					CONTINUES
				</div>

				<div class="auth-heading">
					<h2>
						Welcome <em>back!</em>
					</h2>
					<p>Sign in to discover more of what you love.</p>
				</div>

				<% 
                    String success = request.getParameter("success");
                    if (success != null && !success.trim().isEmpty()) {
                %>
				<div class="auth-message success-message" role="status">
					<%= escapeHtml(success) %>
				</div>
				<% } %>

				<%
                    String error = (String) request.getAttribute("error");
                    if (error != null && !error.trim().isEmpty()) {
                %>
				<div class="auth-message error-message" role="alert">
					<%= escapeHtml(error) %>
				</div>
				<% } %>

				<form id="loginForm" class="auth-form"
					action="${pageContext.request.contextPath}/login" method="post">

					<div class="form-group">
						<label for="email">Email address</label>
						<div class="input-shell">
							<span class="input-icon" aria-hidden="true">✉</span> <input
								type="email" id="email" name="email"
								placeholder="you@example.com" autocomplete="email"
								maxlength="150" required>
						</div>
					</div>

					<div class="form-group">
						<label for="password">Password</label>
						<div class="input-shell password-wrapper">
							<span class="input-icon" aria-hidden="true">♧</span> <input
								type="password" id="password" name="password"
								placeholder="Enter your password"
								autocomplete="current-password" minlength="6" maxlength="255"
								required>
							<button type="button" id="togglePassword" class="password-toggle"
								aria-label="Show password" aria-pressed="false">Show</button>
						</div>
						<small class="field-hint">Enter the password associated
							with your account.</small>
					</div>

					<div class="auth-options">
						<label class="checkbox-label" for="rememberMe"> <input
							type="checkbox" id="rememberMe" name="rememberMe"> <span
							class="custom-checkbox"></span> Remember me
						</label>
					</div>

					<button type="submit" id="loginButton" class="auth-submit">
						<span id="buttonText">Sign In to Zoloto</span> <span
							class="submit-arrow" aria-hidden="true">↗</span>
					</button>
				</form>

				<div class="auth-switch">
					New to Zoloto? <a
						href="${pageContext.request.contextPath}/register.jsp">Create
						an account <span>→</span>
					</a>
				</div>

				<div class="auth-trust">
					<span>✦</span> A little something for every part of your life.
				</div>
			</div>

			<footer class="auth-footer">
				<span>© 2026 Zoloto</span> <span>EVERYTHING YOU LOVE, ALL IN
					ONE PLACE.</span>
			</footer>
		</section>
	</main>

	<script src="${pageContext.request.contextPath}/assets/js/login.js"></script>
</body>
</html>
