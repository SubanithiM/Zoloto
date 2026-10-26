package com.zoloto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Order;
import com.zoloto.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public boolean createOrder(Order order) {

        String sql = "INSERT INTO orders " +
                     "(order_number, user_id, address_id, total_amount, " +
                     "payment_method, payment_status, order_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, order.getOrderNumber());
            ps.setInt(2, order.getUserId());
            ps.setInt(3, order.getAddressId());
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getPaymentMethod());
            ps.setString(6, order.getPaymentStatus());
            ps.setString(7, order.getOrderStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        order.setOrderId(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // READ
    // =====================================================

    @Override
    public Order findById(int orderId) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Order findByOrderNumber(String orderNumber) {

        String sql = "SELECT * FROM orders WHERE order_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, orderNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Order> findAll() {

        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByUserId(int userId) {

        String sql = "SELECT * FROM orders " +
                     "WHERE user_id = ? ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByStatus(String status) {

        String sql = "SELECT * FROM orders " +
                     "WHERE order_status = ? ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByPaymentStatus(String paymentStatus) {

        String sql = "SELECT * FROM orders " +
                     "WHERE payment_status = ? ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByPaymentMethod(String paymentMethod) {

        String sql = "SELECT * FROM orders " +
                     "WHERE payment_method = ? ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    // =====================================================
    // SEARCH / FILTER
    // =====================================================

    @Override
    public List<Order> findByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String sql = "SELECT * FROM orders " +
                     "WHERE order_date BETWEEN ? AND ? " +
                     "ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByUserIdAndStatus(
            int userId,
            String status) {

        String sql = "SELECT * FROM orders " +
                     "WHERE user_id = ? AND order_status = ? " +
                     "ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, status);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByUserIdAndDateRange(
            int userId,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String sql = "SELECT * FROM orders " +
                     "WHERE user_id = ? AND order_date BETWEEN ? AND ? " +
                     "ORDER BY order_date DESC";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(startDate));
            ps.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public List<Order> findByTotalRange(
            BigDecimal minTotal,
            BigDecimal maxTotal) {

        String sql = "SELECT * FROM orders " +
                     "WHERE total_amount BETWEEN ? AND ? " +
                     "ORDER BY total_amount";

        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, minTotal);
            ps.setBigDecimal(2, maxTotal);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    // =====================================================
    // EXISTENCE
    // =====================================================

    @Override
    public boolean existsById(int orderId) {

        String sql = "SELECT COUNT(*) FROM orders WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean existsByOrderNumber(String orderNumber) {

        String sql = "SELECT COUNT(*) FROM orders WHERE order_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, orderNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // COUNT / CALCULATIONS
    // =====================================================

    @Override
    public int countAll() {

        String sql = "SELECT COUNT(*) FROM orders";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
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
    public int countByUserId(int userId) {

        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

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


    @Override
    public int countByStatus(String status) {

        String sql = "SELECT COUNT(*) FROM orders WHERE order_status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

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


    @Override
    public BigDecimal getTotalByUserId(int userId) {

        String sql = "SELECT COALESCE(SUM(total_amount), 0) " +
                     "FROM orders WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }


    @Override
    public BigDecimal getTotalByStatus(String status) {

        String sql = "SELECT COALESCE(SUM(total_amount), 0) " +
                     "FROM orders WHERE order_status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public boolean updateOrder(Order order) {

        String sql = "UPDATE orders SET " +
                     "order_number = ?, " +
                     "user_id = ?, " +
                     "address_id = ?, " +
                     "total_amount = ?, " +
                     "payment_method = ?, " +
                     "payment_status = ?, " +
                     "order_status = ? " +
                     "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, order.getOrderNumber());
            ps.setInt(2, order.getUserId());
            ps.setInt(3, order.getAddressId());
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getPaymentMethod());
            ps.setString(6, order.getPaymentStatus());
            ps.setString(7, order.getOrderStatus());
            ps.setInt(8, order.getOrderId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateStatus(
            int orderId,
            String status) {

        String sql = "UPDATE orders SET order_status = ? " +
                     "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updatePaymentStatus(
            int orderId,
            String paymentStatus) {

        String sql = "UPDATE orders SET payment_status = ? " +
                     "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updatePaymentMethod(
            int orderId,
            String paymentMethod) {

        String sql = "UPDATE orders SET payment_method = ? " +
                     "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateTotalAmount(
            int orderId,
            BigDecimal totalAmount) {

        String sql = "UPDATE orders SET total_amount = ? " +
                     "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, totalAmount);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public boolean deleteOrder(int orderId) {

        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean deleteByUserId(int userId) {

        String sql = "DELETE FROM orders WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // RESULT SET → ORDER
    // =====================================================

    private Order mapResultSetToOrder(ResultSet rs)
            throws SQLException {

        Order order = new Order();

        order.setOrderId(rs.getInt("order_id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setUserId(rs.getInt("user_id"));
        order.setAddressId(rs.getInt("address_id"));

        Timestamp orderTimestamp = rs.getTimestamp("order_date");

        if (orderTimestamp != null) {
            order.setOrderDate(orderTimestamp.toLocalDateTime());
        }

        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setOrderStatus(rs.getString("order_status"));

        return order;
    }
}