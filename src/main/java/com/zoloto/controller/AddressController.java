package com.zoloto.controller;

import java.io.IOException;
import java.util.List;

import com.zoloto.dao.AddressDAO;
import com.zoloto.dao.AddressDAOImpl;
import com.zoloto.model.Address;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/address")
public class AddressController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AddressDAO addressDAO;

    @Override
    public void init() throws ServletException {
        addressDAO = new AddressDAOImpl();
    }

    // GET: Display addresses and load an address for editing
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            String addressIdValue = request.getParameter("addressId");

            try {
                int addressId = Integer.parseInt(addressIdValue);
                Address selectedAddress = addressDAO.findById(addressId);

                if (selectedAddress == null
                        || selectedAddress.getUserId() != userId) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/address?error=invalidAddress");
                    return;
                }

                request.setAttribute("editAddress", selectedAddress);

            } catch (NumberFormatException | NullPointerException e) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/address?error=invalidAddress");
                return;
            }
        }

        List<Address> addresses = addressDAO.findByUserId(userId);

        Address defaultAddress =
                addressDAO.findDefaultAddressByUserId(userId);

        request.setAttribute("addresses", addresses);
        request.setAttribute("defaultAddress", defaultAddress);

        request.getRequestDispatcher("/address.jsp")
               .forward(request, response);
    }

    // POST: Process address operations
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/address");
            return;
        }

        switch (action) {
            case "add":
                addAddress(request, response, userId);
                break;

            case "update":
                updateAddress(request, response, userId);
                break;

            case "delete":
                deleteAddress(request, response, userId);
                break;

            case "default":
            case "setDefault":
                setDefaultAddress(request, response, userId);
                break;

            case "clearDefault":
                clearDefaultAddress(request, response, userId);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/address");
                break;
        }
    }

    // Add a new address
    private void addAddress(HttpServletRequest request,
                            HttpServletResponse response,
                            int userId)
            throws IOException {

        String fullName = request.getParameter("fullName");
        String mobile = request.getParameter("mobile");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String pincode = request.getParameter("pincode");
        String addressType = request.getParameter("addressType");

        boolean defaultAddress = isChecked(
                request.getParameter("defaultAddress"));

        Address address = new Address(
                userId,
                fullName,
                mobile,
                addressLine1,
                addressLine2,
                city,
                state,
                country,
                pincode,
                addressType,
                defaultAddress
        );

        boolean created = addressDAO.createAddress(address);

        if (created && defaultAddress) {
            List<Address> addresses = addressDAO.findByUserId(userId);

            if (!addresses.isEmpty()) {
                Address latestAddress = addresses.get(addresses.size() - 1);

                addressDAO.setDefaultAddress(
                        userId,
                        latestAddress.getAddressId()
                );
            }
        }

        if (created) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?success=added");
        } else {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=addFailed");
        }
    }

    // Update an existing address
    private void updateAddress(HttpServletRequest request,
                               HttpServletResponse response,
                               int userId)
            throws IOException {

        int addressId;

        try {
            addressId = Integer.parseInt(
                    request.getParameter("addressId"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=invalidAddress");
            return;
        }

        Address existingAddress = addressDAO.findById(addressId);

        if (existingAddress == null
                || existingAddress.getUserId() != userId) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=invalidAddress");
            return;
        }

        boolean wasDefaultAddress = existingAddress.isDefaultAddress();

        String fullName = request.getParameter("fullName");
        String mobile = request.getParameter("mobile");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String pincode = request.getParameter("pincode");
        String addressType = request.getParameter("addressType");

        boolean defaultAddress = isChecked(
                request.getParameter("defaultAddress"));

        existingAddress.setFullName(fullName);
        existingAddress.setMobile(mobile);
        existingAddress.setAddressLine1(addressLine1);
        existingAddress.setAddressLine2(addressLine2);
        existingAddress.setCity(city);
        existingAddress.setState(state);
        existingAddress.setCountry(country);
        existingAddress.setPincode(pincode);
        existingAddress.setAddressType(addressType);
        existingAddress.setDefaultAddress(defaultAddress);

        boolean updated = addressDAO.updateAddress(existingAddress);

        if (updated && defaultAddress) {
            addressDAO.setDefaultAddress(userId, addressId);
        } else if (updated && wasDefaultAddress && !defaultAddress) {
            addressDAO.clearDefaultAddress(userId);
        }

        if (updated) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?success=updated");
        } else {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=updateFailed");
        }
    }

    // Delete an address
    private void deleteAddress(HttpServletRequest request,
                               HttpServletResponse response,
                               int userId)
            throws IOException {

        int addressId;

        try {
            addressId = Integer.parseInt(
                    request.getParameter("addressId"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=invalidAddress");
            return;
        }

        Address address = addressDAO.findById(addressId);

        if (address == null || address.getUserId() != userId) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=invalidAddress");
            return;
        }

        boolean deleted = addressDAO.deleteAddress(addressId);

        if (deleted) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?success=deleted");
        } else {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=deleteFailed");
        }
    }

    // Set an address as the default
    private void setDefaultAddress(HttpServletRequest request,
                                   HttpServletResponse response,
                                   int userId)
            throws IOException {

        int addressId;

        try {
            addressId = Integer.parseInt(
                    request.getParameter("addressId"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=invalidAddress");
            return;
        }

        Address address = addressDAO.findById(addressId);

        if (address == null || address.getUserId() != userId) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=invalidAddress");
            return;
        }

        boolean updated = addressDAO.setDefaultAddress(userId, addressId);

        if (updated) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?success=defaultUpdated");
        } else {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=defaultFailed");
        }
    }

    // Clear the default address
    private void clearDefaultAddress(HttpServletRequest request,
                                     HttpServletResponse response,
                                     int userId)
            throws IOException {

        boolean cleared = addressDAO.clearDefaultAddress(userId);

        if (cleared) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?success=defaultCleared");
        } else {
            response.sendRedirect(
                    request.getContextPath()
                    + "/address?error=clearDefaultFailed");
        }
    }

    // Interpret checkbox values consistently
    private boolean isChecked(String value) {
        return "true".equalsIgnoreCase(value)
                || "on".equalsIgnoreCase(value);
    }
}