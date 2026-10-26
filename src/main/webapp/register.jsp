
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Zoloto | Create Account</title>
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css?v=1">

</head>
<body class="auth-body register-body">

    <div class="auth-decoration auth-decoration-one"></div>
    <div class="auth-decoration auth-decoration-two"></div>
    <div class="auth-decoration auth-decoration-three"></div>

    <main class="auth-layout register-layout">

        <!-- Left promotional panel -->
        <section class="auth-showcase register-showcase">
            <a class="showcase-brand" href="${pageContext.request.contextPath}/home">
                <span class="showcase-brand-icon">Z</span>
                <span>zoloto<span class="brand-period">.</span></span>
            </a>

            <div class="showcase-copy">
                <span class="showcase-eyebrow">A LITTLE BIT OF EVERYTHING</span>
                <h1>Find your<br><em>kind of wonderful.</em></h1>
                <p>One account. Endless discoveries. Make room for things that make you smile.</p>

                <div class="showcase-pills">
                    <span>Fresh styles</span>
                    <span>Everyday tech</span>
                    <span>Little luxuries</span>
                </div>
            </div>

            <div class="showcase-art register-art">
                <img
                    src="https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=1000&q=85"
                    alt="Beautifully arranged shopping and lifestyle collection">
                <div class="showcase-art-label">
                    <span class="art-label-dot"></span>
                    <div>
                        <strong>Your next chapter</strong>
                        <small>starts with a little discovery</small>
                    </div>
                    <span class="art-label-star">✦</span>
                </div>
            </div>

            <div class="showcase-bottom">
                <span>MAKE YOURSELF AT HOME</span>
                <span>02 — ZOLOTO</span>
            </div>
        </section>

        <!-- Registration form panel -->
        <section class="auth-form-panel register-form-panel">
            <a class="mobile-brand" href="${pageContext.request.contextPath}/home">
                <span class="showcase-brand-icon">Z</span>
                <span>zoloto<span class="brand-period">.</span></span>
            </a>

            <div class="auth-form-content">
                <div class="auth-top-note">
                    <span class="auth-note-icon">✦</span>
                    YOUR NEXT FAVOURITE THING AWAITS
                </div>

                <div class="auth-heading">
                    <h2>Create your <em>account.</em></h2>
                    <p>Join Zoloto and make shopping feel more you.</p>
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

                <form id="registerForm"
                      class="auth-form"
                      action="${pageContext.request.contextPath}/register"
                      method="post">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="firstName">First name</label>
                            <div class="input-shell">
                                <span class="input-icon" aria-hidden="true">♙</span>
                                <input type="text"
                                       id="firstName"
                                       name="firstName"
                                       placeholder="First name"
                                       autocomplete="given-name"
                                       maxlength="100"
                                       required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="lastName">Last name</label>
                            <div class="input-shell">
                                <span class="input-icon" aria-hidden="true">♙</span>
                                <input type="text"
                                       id="lastName"
                                       name="lastName"
                                       placeholder="Last name"
                                       autocomplete="family-name"
                                       maxlength="100"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email">Email address</label>
                        <div class="input-shell">
                            <span class="input-icon" aria-hidden="true">✉</span>
                            <input type="email"
                                   id="email"
                                   name="email"
                                   placeholder="you@example.com"
                                   autocomplete="email"
                                   maxlength="150"
                                   required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="mobile">Mobile number</label>
                        <div class="input-shell">
                            <span class="input-icon" aria-hidden="true">☎</span>
                            <input type="text"
                                   id="mobile"
                                   name="mobile"
                                   placeholder="Enter 10 digit mobile number"
                                   autocomplete="tel"
                                   inputmode="numeric"
                                   maxlength="10"
                                   pattern="[0-9]{10}"
                                   required>
                        </div>
                        <small class="field-hint" id="mobileMessage">Enter exactly 10 digits.</small>
                    </div>

                    <div class="form-group">
                        <label for="password">Create password</label>
                        <div class="input-shell password-wrapper">
                            <span class="input-icon" aria-hidden="true">♧</span>
                            <input type="password"
                                   id="password"
                                   name="password"
                                   placeholder="Create a password"
                                   autocomplete="new-password"
                                   minlength="6"
                                   maxlength="255"
                                   required>
                            <button type="button"
                                    id="togglePassword"
                                    class="password-toggle"
                                    aria-label="Show password"
                                    aria-pressed="false">Show</button>
                        </div>
                        <small class="field-hint">Use at least 6 characters.</small>
                    </div>

                    <div class="auth-options terms-options">
                        <label class="checkbox-label" for="terms">
                            <input type="checkbox" id="terms" name="terms" required>
                            <span class="custom-checkbox"></span>
                            <span>I agree to the terms and conditions.</span>
                        </label>
                    </div>

                    <button type="submit" id="registerButton" class="auth-submit">
                        <span id="buttonText">Create My Account</span>
                        <span class="submit-arrow" aria-hidden="true">↗</span>
                    </button>
                </form>

                <div class="auth-switch">
                    Already have an account?
                    <a href="${pageContext.request.contextPath}/login">Sign in <span>→</span></a>
                </div>

                <div class="auth-trust">
                    <span>✦</span>
                    Everything you love, all in one place.
                </div>
            </div>

            <footer class="auth-footer">
                <span>© 2026 Zoloto</span>
                <span>MADE FOR YOUR EVERYDAY.</span>
            </footer>
        </section>
    </main>

    <script src="${pageContext.request.contextPath}/assets/js/register.js"></script>
</body>
</html>
