
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zoloto.model.Address" %>

<%
    List<Address> addresses =
        (List<Address>) request.getAttribute("addresses");

    Address editAddress =
        (Address) request.getAttribute("editAddress");

    String message = request.getParameter("message");
    String error = request.getParameter("error");

    if (addresses == null) {
        addresses = java.util.Collections.emptyList();
    }

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
        content="width=device-width, initial-scale=1.0">

    <title>My Addresses | Zoloto</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/address.css">
</head>

<body>

<header class="site-header">
    <a class="brand" href="<%= contextPath %>/home">
        <span class="brand-mark">Z</span>
        <span>zoloto</span>
    </a>

    <nav class="main-nav">
        <a href="<%= contextPath %>/home">Home</a>
        <a href="<%= contextPath %>/shop">Shop</a>
        <a href="<%= contextPath %>/categories">Categories</a>
        <a href="<%= contextPath %>/cart">Cart</a>
    </nav>

    <a class="logout-link"
       href="<%= contextPath %>/logout">Logout</a>
</header>

<main class="page-container">

    <section class="page-heading">
        <div>
            <span class="eyebrow">YOUR ACCOUNT</span>
            <h1>My addresses<span>.</span></h1>
            <p>Manage your delivery details in one place.</p>
        </div>

        <div class="address-count">
            <span class="count-number"><%= addresses.size() %></span>
            <span class="count-label">
                Saved <br>addresses
            </span>
        </div>
    </section>

    <% if (message != null) { %>
        <div class="notice success-notice">
            <%= "success".equals(message)
                    ? "Your address was updated successfully."
                    : "Your request has been processed." %>
        </div>
    <% } %>

    <% if (error != null) { %>
        <div class="notice error-notice">
            We couldn't complete that request. Please try again.
        </div>
    <% } %>

    <div class="content-grid">

        <section class="address-list-section">
            <div class="section-heading">
                <div>
                    <span class="eyebrow">DELIVERY DETAILS</span>
                    <h2>Saved addresses</h2>
                </div>
            </div>

            <% if (addresses.isEmpty()) { %>

                <div class="empty-state">
                    <div class="empty-icon">&#8962;</div>
                    <h3>No addresses yet</h3>
                    <p>Add your first delivery address to get started.</p>
                    <a href="#address-form" class="primary-button">
                        + Add an address
                    </a>
                </div>

            <% } else { %>

                <div class="address-cards">

                    <% for (Address address : addresses) { %>

                        <article class="address-card">

                            <div class="card-top">
                                <div class="address-type">
                                    <span class="type-icon">
                                        <%= "Work".equalsIgnoreCase(
                                                address.getAddressType())
                                                ? "&#128188;" : "&#8962;" %>
                                    </span>

                                    <span>
                                        <%= address.getAddressType() == null
                                                ? "Other"
                                                : address.getAddressType() %>
                                    </span>
                                </div>

                                <% if (address.isDefaultAddress()) { %>
                                    <span class="default-badge">
                                        Default address
                                    </span>
                                <% } %>
                            </div>

                            <h3 class="recipient-name">
                                <%= address.getFullName() == null
                                        ? "" : address.getFullName() %>
                            </h3>

                            <p class="recipient-mobile">
                                <%= address.getMobile() == null
                                        ? "" : address.getMobile() %>
                            </p>

                            <p class="address-text">
                                <%= address.getAddressLine1() == null
                                        ? "" : address.getAddressLine1() %>

                                <% if (address.getAddressLine2() != null
                                        && !address.getAddressLine2()
                                                .trim().isEmpty()) { %>
                                    <br>
                                    <%= address.getAddressLine2() %>
                                <% } %>

                                <br>
                                <%= address.getCity() == null
                                        ? "" : address.getCity() %>,
                                <%= address.getState() == null
                                        ? "" : address.getState() %>
                                <%= address.getPincode() == null
                                        ? "" : address.getPincode() %>
                                <br>
                                <%= address.getCountry() == null
                                        ? "" : address.getCountry() %>
                            </p>

                            <div class="card-actions">

                                <a class="secondary-button"
                                   href="<%= contextPath %>/address?action=edit&addressId=<%= address.getAddressId() %>">
                                    Edit
                                </a>

                                <% if (!address.isDefaultAddress()) { %>
                                    <form method="post"
                                          action="<%= contextPath %>/address"
                                          class="inline-form">

                                        <input type="hidden"
                                               name="action"
                                               value="default">

                                        <input type="hidden"
                                               name="addressId"
                                               value="<%= address.getAddressId() %>">

                                        <button type="submit"
                                                class="text-button">
                                            Set as default
                                        </button>
                                    </form>
                                <% } %>

                                <form method="post"
                                      action="<%= contextPath %>/address"
                                      class="inline-form"
                                      onsubmit="return confirm('Delete this address?');">

                                    <input type="hidden"
                                           name="action"
                                           value="delete">

                                    <input type="hidden"
                                           name="addressId"
                                           value="<%= address.getAddressId() %>">

                                    <button type="submit"
                                            class="delete-button">
                                        Delete
                                    </button>
                                </form>

                            </div>
                        </article>

                    <% } %>

                </div>

            <% } %>
        </section>

        <aside class="form-panel" id="address-form">

            <div class="form-heading">
                <span class="eyebrow">
                    <%= editAddress == null
                            ? "NEW DELIVERY LOCATION"
                            : "UPDATE YOUR DETAILS" %>
                </span>

                <h2>
                    <%= editAddress == null
                            ? "Add an address"
                            : "Edit address" %>
                </h2>

                <p>
                    Enter the details exactly as they should appear
                    on your delivery.
                </p>
            </div>

            <form method="post"
                  action="<%= contextPath %>/address"
                  class="address-form">

                <input type="hidden"
                       name="action"
                       value="<%= editAddress == null
                                    ? "add" : "update" %>">

                <% if (editAddress != null) { %>
                    <input type="hidden"
                           name="addressId"
                           value="<%= editAddress.getAddressId() %>">
                <% } %>

                <div class="form-field">
                    <label for="fullName">Full name</label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           maxlength="100"
                           value="<%= editAddress == null
                                || editAddress.getFullName() == null
                                ? "" : editAddress.getFullName() %>"
                           placeholder="Enter recipient's name"
                           autocomplete="name"
                           required>
                </div>

                <div class="form-field">
                    <label for="mobile">Mobile number</label>
                    <input type="tel"
                           id="mobile"
                           name="mobile"
                           maxlength="20"
                           value="<%= editAddress == null
                                || editAddress.getMobile() == null
                                ? "" : editAddress.getMobile() %>"
                           placeholder="Enter mobile number"
                           autocomplete="tel"
                           required>
                </div>

                <div class="form-field">
                    <label for="addressLine1">Address line 1</label>
                    <input type="text"
                           id="addressLine1"
                           name="addressLine1"
                           maxlength="255"
                           value="<%= editAddress == null
                                || editAddress.getAddressLine1() == null
                                ? "" : editAddress.getAddressLine1() %>"
                           placeholder="House number, street"
                           autocomplete="address-line1"
                           required>
                </div>

                <div class="form-field">
                    <label for="addressLine2">
                        Address line 2
                        <span class="optional">Optional</span>
                    </label>
                    <input type="text"
                           id="addressLine2"
                           name="addressLine2"
                           maxlength="255"
                           value="<%= editAddress == null
                                || editAddress.getAddressLine2() == null
                                ? "" : editAddress.getAddressLine2() %>"
                           placeholder="Apartment, landmark"
                           autocomplete="address-line2">
                </div>

                <div class="form-row">
                    <div class="form-field">
                        <label for="city">City</label>
                        <input type="text"
                               id="city"
                               name="city"
                               maxlength="100"
                               value="<%= editAddress == null
                                    || editAddress.getCity() == null
                                    ? "" : editAddress.getCity() %>"
                               autocomplete="address-level2"
                               required>
                    </div>

                    <div class="form-field">
                        <label for="state">State</label>
                        <input type="text"
                               id="state"
                               name="state"
                               maxlength="100"
                               value="<%= editAddress == null
                                    || editAddress.getState() == null
                                    ? "" : editAddress.getState() %>"
                               autocomplete="address-level1"
                               required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-field">
                        <label for="pincode">PIN code</label>
                        <input type="text"
                               id="pincode"
                               name="pincode"
                               maxlength="15"
                               value="<%= editAddress == null
                                    || editAddress.getPincode() == null
                                    ? "" : editAddress.getPincode() %>"
                               autocomplete="postal-code"
                               required>
                    </div>

                    <div class="form-field">
                        <label for="country">Country</label>
                        <input type="text"
                               id="country"
                               name="country"
                               maxlength="100"
                               value="<%= editAddress == null
                                    || editAddress.getCountry() == null
                                    ? "India"
                                    : editAddress.getCountry() %>"
                               autocomplete="country-name"
                               required>
                    </div>
                </div>

                <div class="form-field">
                    <label for="addressType">Address type</label>
                    <select id="addressType"
                            name="addressType"
                            required>

                        <option value="Home"
                            <%= editAddress != null
                                && "Home".equalsIgnoreCase(
                                    editAddress.getAddressType())
                                ? "selected" : "" %>>
                            Home
                        </option>

                        <option value="Work"
                            <%= editAddress != null
                                && "Work".equalsIgnoreCase(
                                    editAddress.getAddressType())
                                ? "selected" : "" %>>
                            Work
                        </option>

                        <option value="Other"
                            <%= editAddress != null
                                && "Other".equalsIgnoreCase(
                                    editAddress.getAddressType())
                                ? "selected" : "" %>>
                            Other
                        </option>
                    </select>
                </div>

                <label class="checkbox-row">
                    <input type="checkbox"
                           name="defaultAddress"
                           value="true"
                           <%= editAddress != null
                                && editAddress.isDefaultAddress()
                                ? "checked" : "" %>>
                    <span>Make this my default address</span>
                </label>

                <button type="submit" class="primary-button submit-button">
                    <%= editAddress == null
                            ? "Save address" : "Update address" %>
                    <span aria-hidden="true">&rarr;</span>
                </button>

                <% if (editAddress != null) { %>
                    <a class="cancel-link"
                       href="<%= contextPath %>/address">
                        Cancel editing
                    </a>
                <% } %>

            </form>
        </aside>

    </div>

    <footer class="page-footer">
        <span class="footer-brand">zoloto.</span>
        <span>Thoughtfully delivered, right to your door.</span>
    </footer>

</main>

</body>
</html>
