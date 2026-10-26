package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Shipping;
import com.zoloto.util.DBConnection;

public class ShippingDAOImpl implements ShippingDAO {

    // =========================
    // Create
    // =========================

    @Override
    public boolean createShipping(Shipping shipping) {

        String sql = "INSERT INTO shipping "
                + "(order_id, courier_name, tracking_number, shipping_date, "
                + "expected_delivery, delivery_status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, shipping.getOrderId());

            ps.setString(2, shipping.getCourierName());

            ps.setString(3, shipping.getTrackingNumber());

            if (shipping.getShippingDate() != null) {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(shipping.getShippingDate()));
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            }

            if (shipping.getExpectedDelivery() != null) {
                ps.setTimestamp(
                        5,
                        Timestamp.valueOf(shipping.getExpectedDelivery()));
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            }

            ps.setString(6, shipping.getDeliveryStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        shipping.setShippingId(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Read
    // =========================

    @Override
    public Shipping findById(int shippingId) {

        String sql = "SELECT * FROM shipping WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, shippingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToShipping(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Shipping findByOrderId(int orderId) {

        String sql = "SELECT * FROM shipping "
                + "WHERE order_id = ? "
                + "ORDER BY shipping_id DESC "
                + "LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToShipping(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Shipping findByTrackingNumber(String trackingNumber) {

        String sql = "SELECT * FROM shipping "
                + "WHERE tracking_number = ? "
                + "LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, trackingNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToShipping(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Shipping> findAll() {

        List<Shipping> shippingList = new ArrayList<>();

        String sql = "SELECT * FROM shipping "
                + "ORDER BY shipping_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                shippingList.add(mapResultSetToShipping(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shippingList;
    }

    @Override
    public List<Shipping> findByStatus(String deliveryStatus) {

        List<Shipping> shippingList = new ArrayList<>();

        String sql = "SELECT * FROM shipping "
                + "WHERE delivery_status = ? "
                + "ORDER BY shipping_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, deliveryStatus);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    shippingList.add(mapResultSetToShipping(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shippingList;
    }

    @Override
    public List<Shipping> findByCourier(String courierName) {

        List<Shipping> shippingList = new ArrayList<>();

        String sql = "SELECT * FROM shipping "
                + "WHERE courier_name = ? "
                + "ORDER BY shipping_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, courierName);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    shippingList.add(mapResultSetToShipping(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shippingList;
    }

    // =========================
    // Search / Filtering
    // =========================

    @Override
    public List<Shipping> findByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        List<Shipping> shippingList = new ArrayList<>();

        String sql = "SELECT * FROM shipping "
                + "WHERE shipping_date BETWEEN ? AND ? "
                + "ORDER BY shipping_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    shippingList.add(mapResultSetToShipping(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shippingList;
    }

    @Override
    public List<Shipping> findByStatusAndDateRange(
            String deliveryStatus,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        List<Shipping> shippingList = new ArrayList<>();

        String sql = "SELECT * FROM shipping "
                + "WHERE delivery_status = ? "
                + "AND shipping_date BETWEEN ? AND ? "
                + "ORDER BY shipping_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, deliveryStatus);
            ps.setTimestamp(2, Timestamp.valueOf(startDate));
            ps.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    shippingList.add(mapResultSetToShipping(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shippingList;
    }

    // =========================
    // Existence
    // =========================

    @Override
    public boolean existsById(int shippingId) {

        String sql = "SELECT 1 FROM shipping "
                + "WHERE shipping_id = ? "
                + "LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, shippingId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean existsByOrderId(int orderId) {

        String sql = "SELECT 1 FROM shipping "
                + "WHERE order_id = ? "
                + "LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean existsByTrackingNumber(String trackingNumber) {

        String sql = "SELECT 1 FROM shipping "
                + "WHERE tracking_number = ? "
                + "LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, trackingNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Update
    // =========================

    @Override
    public boolean updateShipping(Shipping shipping) {

        String sql = "UPDATE shipping SET "
                + "order_id = ?, "
                + "courier_name = ?, "
                + "tracking_number = ?, "
                + "shipping_date = ?, "
                + "expected_delivery = ?, "
                + "delivery_status = ? "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, shipping.getOrderId());
            ps.setString(2, shipping.getCourierName());
            ps.setString(3, shipping.getTrackingNumber());

            if (shipping.getShippingDate() != null) {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(shipping.getShippingDate()));
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            }

            if (shipping.getExpectedDelivery() != null) {
                ps.setTimestamp(
                        5,
                        Timestamp.valueOf(shipping.getExpectedDelivery()));
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            }

            ps.setString(6, shipping.getDeliveryStatus());
            ps.setInt(7, shipping.getShippingId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateCourier(
            int shippingId,
            String courierName) {

        String sql = "UPDATE shipping "
                + "SET courier_name = ? "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, courierName);
            ps.setInt(2, shippingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateTrackingNumber(
            int shippingId,
            String trackingNumber) {

        String sql = "UPDATE shipping "
                + "SET tracking_number = ? "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, trackingNumber);
            ps.setInt(2, shippingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateShippingDate(
            int shippingId,
            LocalDateTime shippingDate) {

        String sql = "UPDATE shipping "
                + "SET shipping_date = ? "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (shippingDate != null) {
                ps.setTimestamp(
                        1,
                        Timestamp.valueOf(shippingDate));
            } else {
                ps.setNull(1, java.sql.Types.TIMESTAMP);
            }

            ps.setInt(2, shippingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateExpectedDelivery(
            int shippingId,
            LocalDateTime expectedDelivery) {

        String sql = "UPDATE shipping "
                + "SET expected_delivery = ? "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (expectedDelivery != null) {
                ps.setTimestamp(
                        1,
                        Timestamp.valueOf(expectedDelivery));
            } else {
                ps.setNull(1, java.sql.Types.TIMESTAMP);
            }

            ps.setInt(2, shippingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateDeliveryStatus(
            int shippingId,
            String deliveryStatus) {

        String sql = "UPDATE shipping "
                + "SET delivery_status = ? "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, deliveryStatus);
            ps.setInt(2, shippingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Delete
    // =========================

    @Override
    public boolean deleteShipping(int shippingId) {

        String sql = "DELETE FROM shipping "
                + "WHERE shipping_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, shippingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteByOrderId(int orderId) {

        String sql = "DELETE FROM shipping "
                + "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // Count
    // =========================

    @Override
    public int countAll() {

        String sql = "SELECT COUNT(*) FROM shipping";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int countByStatus(String deliveryStatus) {

        String sql = "SELECT COUNT(*) FROM shipping "
                + "WHERE delivery_status = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, deliveryStatus);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =========================
    // Helper Method
    // =========================

    private Shipping mapResultSetToShipping(ResultSet rs)
            throws SQLException {

        Shipping shipping = new Shipping();

        shipping.setShippingId(
                rs.getInt("shipping_id"));

        shipping.setOrderId(
                rs.getInt("order_id"));

        shipping.setCourierName(
                rs.getString("courier_name"));

        shipping.setTrackingNumber(
                rs.getString("tracking_number"));

        Timestamp shippingTimestamp =
                rs.getTimestamp("shipping_date");

        if (shippingTimestamp != null) {
            shipping.setShippingDate(
                    shippingTimestamp.toLocalDateTime());
        } else {
            shipping.setShippingDate(null);
        }

        Timestamp expectedDeliveryTimestamp =
                rs.getTimestamp("expected_delivery");

        if (expectedDeliveryTimestamp != null) {
            shipping.setExpectedDelivery(
                    expectedDeliveryTimestamp.toLocalDateTime());
        } else {
            shipping.setExpectedDelivery(null);
        }

        shipping.setDeliveryStatus(
                rs.getString("delivery_status"));

        return shipping;
    }
}