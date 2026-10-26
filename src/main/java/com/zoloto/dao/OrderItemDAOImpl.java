package com.zoloto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.OrderItem;
import com.zoloto.util.DBConnection;

public class OrderItemDAOImpl implements OrderItemDAO {

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public boolean createOrderItem(OrderItem orderItem) {

        String sql = "INSERT INTO order_items " +
                     "(order_id, product_id, product_name, quantity, " +
                     "unit_price, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());
            ps.setString(3, orderItem.getProductName());
            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getUnitPrice());
            ps.setBigDecimal(6, orderItem.getTotalPrice());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        orderItem.setOrderItemId(rs.getInt(1));
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
    public OrderItem findById(int orderItemId) {

        String sql = "SELECT * FROM order_items " +
                     "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToOrderItem(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public OrderItem findByOrderIdAndProductId(
            int orderId,
            int productId) {

        String sql = "SELECT * FROM order_items " +
                     "WHERE order_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapResultSetToOrderItem(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<OrderItem> findByOrderId(int orderId) {

        String sql = "SELECT * FROM order_items " +
                     "WHERE order_id = ? " +
                     "ORDER BY order_item_id";

        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orderItems.add(mapResultSetToOrderItem(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }


    @Override
    public List<OrderItem> findByProductId(int productId) {

        String sql = "SELECT * FROM order_items " +
                     "WHERE product_id = ? " +
                     "ORDER BY order_item_id";

        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orderItems.add(mapResultSetToOrderItem(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }


    @Override
    public List<OrderItem> findAll() {

        String sql = "SELECT * FROM order_items " +
                     "ORDER BY order_item_id";

        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orderItems.add(mapResultSetToOrderItem(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }


    // =====================================================
    // EXISTENCE / COUNT
    // =====================================================

    @Override
    public boolean existsById(int orderItemId) {

        String sql = "SELECT COUNT(*) FROM order_items " +
                     "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

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
    public boolean existsByOrderIdAndProductId(
            int orderId,
            int productId) {

        String sql = "SELECT COUNT(*) FROM order_items " +
                     "WHERE order_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, productId);

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
    public int countByOrderId(int orderId) {

        String sql = "SELECT COUNT(*) FROM order_items " +
                     "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

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


    // =====================================================
    // CALCULATIONS
    // =====================================================

    @Override
    public BigDecimal getTotalByOrderId(int orderId) {

        String sql = "SELECT COALESCE(SUM(total_price), 0) " +
                     "FROM order_items " +
                     "WHERE order_id = ?";

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
    public boolean updateOrderItem(OrderItem orderItem) {

        String sql = "UPDATE order_items SET " +
                     "order_id = ?, " +
                     "product_id = ?, " +
                     "product_name = ?, " +
                     "quantity = ?, " +
                     "unit_price = ?, " +
                     "total_price = ? " +
                     "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());
            ps.setString(3, orderItem.getProductName());
            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getUnitPrice());
            ps.setBigDecimal(6, orderItem.getTotalPrice());
            ps.setInt(7, orderItem.getOrderItemId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateQuantity(
            int orderItemId,
            int quantity) {

        String sql = "UPDATE order_items SET " +
                     "quantity = ? " +
                     "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, orderItemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateUnitPrice(
            int orderItemId,
            BigDecimal unitPrice) {

        String sql = "UPDATE order_items SET " +
                     "unit_price = ? " +
                     "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, unitPrice);
            ps.setInt(2, orderItemId);

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
    public boolean deleteOrderItem(int orderItemId) {

        String sql = "DELETE FROM order_items " +
                     "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean deleteByOrderIdAndProductId(
            int orderId,
            int productId) {

        String sql = "DELETE FROM order_items " +
                     "WHERE order_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean deleteByOrderId(int orderId) {

        String sql = "DELETE FROM order_items " +
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


    // =====================================================
    // RESULT SET → ORDER ITEM
    // =====================================================

    private OrderItem mapResultSetToOrderItem(ResultSet rs)
            throws SQLException {

        OrderItem orderItem = new OrderItem();

        orderItem.setOrderItemId(
                rs.getInt("order_item_id"));

        orderItem.setOrderId(
                rs.getInt("order_id"));

        orderItem.setProductId(
                rs.getInt("product_id"));

        orderItem.setProductName(
                rs.getString("product_name"));

        orderItem.setQuantity(
                rs.getInt("quantity"));

        orderItem.setUnitPrice(
                rs.getBigDecimal("unit_price"));

        orderItem.setTotalPrice(
                rs.getBigDecimal("total_price"));

        return orderItem;
    }
}