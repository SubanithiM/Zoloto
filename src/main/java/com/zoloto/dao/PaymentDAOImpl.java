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

import com.zoloto.model.Payment;
import com.zoloto.util.DBConnection;

public class PaymentDAOImpl implements PaymentDAO {

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public boolean createPayment(Payment payment) {

        String sql = "INSERT INTO payments " +
                     "(order_id, payment_method, transaction_id, " +
                     "amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getTransactionId());
            ps.setBigDecimal(4, payment.getAmount());
            ps.setString(5, payment.getPaymentStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        payment.setPaymentId(rs.getInt(1));
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
    public Payment findById(int paymentId) {

        String sql = "SELECT * FROM payments " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Payment findByOrderId(int orderId) {

        String sql = "SELECT * FROM payments " +
                     "WHERE order_id = ? " +
                     "ORDER BY payment_date DESC " +
                     "LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Payment> findAll() {

        String sql = "SELECT * FROM payments " +
                     "ORDER BY payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> findByUserId(int userId) {

        String sql = "SELECT p.* " +
                     "FROM payments p " +
                     "INNER JOIN orders o ON p.order_id = o.order_id " +
                     "WHERE o.user_id = ? " +
                     "ORDER BY p.payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> findByStatus(String paymentStatus) {

        String sql = "SELECT * FROM payments " +
                     "WHERE payment_status = ? " +
                     "ORDER BY payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> findByMethod(String paymentMethod) {

        String sql = "SELECT * FROM payments " +
                     "WHERE payment_method = ? " +
                     "ORDER BY payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> findByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        String sql = "SELECT * FROM payments " +
                     "WHERE payment_date BETWEEN ? AND ? " +
                     "ORDER BY payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    // =====================================================
    // SEARCH / FILTERING
    // =====================================================

    @Override
    public List<Payment> findByUserIdAndStatus(
            int userId,
            String paymentStatus) {

        String sql = "SELECT p.* " +
                     "FROM payments p " +
                     "INNER JOIN orders o ON p.order_id = o.order_id " +
                     "WHERE o.user_id = ? " +
                     "AND p.payment_status = ? " +
                     "ORDER BY p.payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, paymentStatus);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> findByUserIdAndMethod(
            int userId,
            String paymentMethod) {

        String sql = "SELECT p.* " +
                     "FROM payments p " +
                     "INNER JOIN orders o ON p.order_id = o.order_id " +
                     "WHERE o.user_id = ? " +
                     "AND p.payment_method = ? " +
                     "ORDER BY p.payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, paymentMethod);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    // =====================================================
    // EXISTENCE
    // =====================================================

    @Override
    public boolean existsById(int paymentId) {

        String sql = "SELECT COUNT(*) FROM payments " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

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
    public boolean existsByOrderId(int orderId) {

        String sql = "SELECT COUNT(*) FROM payments " +
                     "WHERE order_id = ?";

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


    // =====================================================
    // COUNT / CALCULATION
    // =====================================================

    @Override
    public int countByUserId(int userId) {

        String sql = "SELECT COUNT(*) " +
                     "FROM payments p " +
                     "INNER JOIN orders o ON p.order_id = o.order_id " +
                     "WHERE o.user_id = ?";

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
    public int countByStatus(String paymentStatus) {

        String sql = "SELECT COUNT(*) " +
                     "FROM payments " +
                     "WHERE payment_status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);

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
    public BigDecimal getTotalPaidByUserId(int userId) {

        String sql = "SELECT COALESCE(SUM(p.amount), 0) " +
                     "FROM payments p " +
                     "INNER JOIN orders o ON p.order_id = o.order_id " +
                     "WHERE o.user_id = ? " +
                     "AND p.payment_status = 'PAID'";

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
    public BigDecimal getTotalPaidByOrderId(int orderId) {

        String sql = "SELECT COALESCE(SUM(amount), 0) " +
                     "FROM payments " +
                     "WHERE order_id = ? " +
                     "AND payment_status = 'PAID'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

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
    public boolean updatePayment(Payment payment) {

        String sql = "UPDATE payments SET " +
                     "order_id = ?, " +
                     "payment_method = ?, " +
                     "transaction_id = ?, " +
                     "amount = ?, " +
                     "payment_status = ? " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getTransactionId());
            ps.setBigDecimal(4, payment.getAmount());
            ps.setString(5, payment.getPaymentStatus());
            ps.setInt(6, payment.getPaymentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateStatus(
            int paymentId,
            String paymentStatus) {

        String sql = "UPDATE payments SET " +
                     "payment_status = ? " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateTransactionId(
            int paymentId,
            String transactionId) {

        String sql = "UPDATE payments SET " +
                     "transaction_id = ? " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, transactionId);
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updatePaymentMethod(
            int paymentId,
            String paymentMethod) {

        String sql = "UPDATE payments SET " +
                     "payment_method = ? " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentMethod);
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateAmount(
            int paymentId,
            BigDecimal amount) {

        String sql = "UPDATE payments SET " +
                     "amount = ? " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, amount);
            ps.setInt(2, paymentId);

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
    public boolean deletePayment(int paymentId) {

        String sql = "DELETE FROM payments " +
                     "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean deleteByOrderId(int orderId) {

        String sql = "DELETE FROM payments " +
                     "WHERE order_id = ?";

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

        String sql = "DELETE p " +
                     "FROM payments p " +
                     "INNER JOIN orders o ON p.order_id = o.order_id " +
                     "WHERE o.user_id = ?";

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
    // RESULT SET → PAYMENT
    // =====================================================

    private Payment mapResultSetToPayment(ResultSet rs)
            throws SQLException {

        Payment payment = new Payment();

        payment.setPaymentId(
                rs.getInt("payment_id"));

        payment.setOrderId(
                rs.getInt("order_id"));

        payment.setPaymentMethod(
                rs.getString("payment_method"));

        payment.setTransactionId(
                rs.getString("transaction_id"));

        payment.setAmount(
                rs.getBigDecimal("amount"));

        payment.setPaymentStatus(
                rs.getString("payment_status"));

        Timestamp paymentTimestamp =
                rs.getTimestamp("payment_date");

        if (paymentTimestamp != null) {
            payment.setPaymentDate(
                    paymentTimestamp.toLocalDateTime());
        }

        return payment;
    }
}