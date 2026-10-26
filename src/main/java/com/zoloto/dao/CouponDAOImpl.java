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

import com.zoloto.model.Coupon;
import com.zoloto.util.DBConnection;

public class CouponDAOImpl implements CouponDAO {

    // =====================================================
    // SQL QUERIES
    // =====================================================

    private static final String INSERT_COUPON_SQL =
            "INSERT INTO coupons " +
            "(coupon_code, description, discount_type, discount_value, " +
            "minimum_order_amount, start_date, end_date, usage_limit, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons WHERE coupon_id = ?";

    private static final String FIND_BY_CODE_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons WHERE coupon_code = ?";

    private static final String FIND_ALL_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons ORDER BY coupon_id";

    private static final String FIND_BY_STATUS_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons WHERE status = ? ORDER BY coupon_id";

    private static final String FIND_ACTIVE_COUPONS_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons " +
            "WHERE status = 'ACTIVE' " +
            "ORDER BY coupon_id";

    private static final String FIND_BY_DISCOUNT_TYPE_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons WHERE discount_type = ? " +
            "ORDER BY coupon_id";

    private static final String FIND_BY_MINIMUM_ORDER_AMOUNT_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons " +
            "WHERE minimum_order_amount <= ? " +
            "ORDER BY coupon_id";

    private static final String FIND_VALID_COUPONS_SQL =
            "SELECT coupon_id, coupon_code, description, discount_type, " +
            "discount_value, minimum_order_amount, start_date, end_date, " +
            "usage_limit, status " +
            "FROM coupons " +
            "WHERE status = 'ACTIVE' " +
            "AND (start_date IS NULL OR start_date <= ?) " +
            "AND (end_date IS NULL OR end_date >= ?) " +
            "ORDER BY coupon_id";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT COUNT(*) FROM coupons WHERE coupon_id = ?";

    private static final String EXISTS_BY_CODE_SQL =
            "SELECT COUNT(*) FROM coupons WHERE coupon_code = ?";

    private static final String IS_VALID_COUPON_SQL =
            "SELECT COUNT(*) FROM coupons " +
            "WHERE coupon_code = ? " +
            "AND status = 'ACTIVE' " +
            "AND (minimum_order_amount IS NULL OR minimum_order_amount <= ?) " +
            "AND (start_date IS NULL OR start_date <= ?) " +
            "AND (end_date IS NULL OR end_date >= ?) " +
            "AND (usage_limit = 0 OR usage_limit IS NULL OR usage_limit > 0)";

    private static final String COUNT_ALL_SQL =
            "SELECT COUNT(*) FROM coupons";

    private static final String COUNT_BY_STATUS_SQL =
            "SELECT COUNT(*) FROM coupons WHERE status = ?";

    private static final String UPDATE_COUPON_SQL =
            "UPDATE coupons SET " +
            "coupon_code = ?, " +
            "description = ?, " +
            "discount_type = ?, " +
            "discount_value = ?, " +
            "minimum_order_amount = ?, " +
            "start_date = ?, " +
            "end_date = ?, " +
            "usage_limit = ?, " +
            "status = ? " +
            "WHERE coupon_id = ?";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE coupons SET status = ? WHERE coupon_id = ?";

    private static final String UPDATE_DISCOUNT_VALUE_SQL =
            "UPDATE coupons SET discount_value = ? WHERE coupon_id = ?";

    private static final String UPDATE_USAGE_LIMIT_SQL =
            "UPDATE coupons SET usage_limit = ? WHERE coupon_id = ?";

    private static final String UPDATE_DATES_SQL =
            "UPDATE coupons SET start_date = ?, end_date = ? " +
            "WHERE coupon_id = ?";

    private static final String DELETE_COUPON_SQL =
            "DELETE FROM coupons WHERE coupon_id = ?";


    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public boolean createCoupon(Coupon coupon) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_COUPON_SQL)) {

            statement.setString(1, coupon.getCouponCode());
            statement.setString(2, coupon.getDescription());
            statement.setString(3, coupon.getDiscountType());
            statement.setBigDecimal(4, coupon.getDiscountValue());
            statement.setBigDecimal(5, coupon.getMinimumOrderAmount());

            setLocalDateTime(statement, 6, coupon.getStartDate());
            setLocalDateTime(statement, 7, coupon.getEndDate());

            statement.setInt(8, coupon.getUsageLimit());
            statement.setString(9, coupon.getStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =====================================================
    // READ
    // =====================================================

    @Override
    public Coupon findById(int couponId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, couponId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCoupon(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Coupon findByCode(String couponCode) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_CODE_SQL)) {

            statement.setString(1, couponCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCoupon(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Coupon> findAll() {

        List<Coupon> coupons = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                coupons.add(mapResultSetToCoupon(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coupons;
    }


    @Override
    public List<Coupon> findByStatus(String status) {

        List<Coupon> coupons = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_STATUS_SQL)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    coupons.add(mapResultSetToCoupon(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coupons;
    }


    @Override
    public List<Coupon> findActiveCoupons() {

        List<Coupon> coupons = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_ACTIVE_COUPONS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                coupons.add(mapResultSetToCoupon(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coupons;
    }


    // =====================================================
    // SEARCH / FILTER
    // =====================================================

    @Override
    public List<Coupon> findByDiscountType(
            String discountType) {

        List<Coupon> coupons = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_DISCOUNT_TYPE_SQL)) {

            statement.setString(1, discountType);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    coupons.add(mapResultSetToCoupon(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coupons;
    }


    @Override
    public List<Coupon> findByMinimumOrderAmount(
            BigDecimal minimumOrderAmount) {

        List<Coupon> coupons = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_MINIMUM_ORDER_AMOUNT_SQL)) {

            statement.setBigDecimal(
                    1,
                    minimumOrderAmount);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    coupons.add(mapResultSetToCoupon(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coupons;
    }


    @Override
    public List<Coupon> findValidCoupons(
            LocalDateTime dateTime) {

        List<Coupon> coupons = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_VALID_COUPONS_SQL)) {

            Timestamp timestamp = null;

            if (dateTime != null) {
                timestamp = Timestamp.valueOf(dateTime);
            }

            statement.setTimestamp(1, timestamp);
            statement.setTimestamp(2, timestamp);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    coupons.add(mapResultSetToCoupon(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coupons;
    }


    // =====================================================
    // EXISTENCE
    // =====================================================

    @Override
    public boolean existsById(int couponId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             EXISTS_BY_ID_SQL)) {

            statement.setInt(1, couponId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean existsByCode(String couponCode) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             EXISTS_BY_CODE_SQL)) {

            statement.setString(1, couponCode);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // VALIDATION
    // =====================================================

    @Override
    public boolean isValidCoupon(
            String couponCode,
            BigDecimal orderAmount,
            LocalDateTime dateTime) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             IS_VALID_COUPON_SQL)) {

            statement.setString(1, couponCode);
            statement.setBigDecimal(2, orderAmount);

            Timestamp timestamp = null;

            if (dateTime != null) {
                timestamp = Timestamp.valueOf(dateTime);
            }

            statement.setTimestamp(3, timestamp);
            statement.setTimestamp(4, timestamp);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public boolean updateCoupon(Coupon coupon) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_COUPON_SQL)) {

            statement.setString(1, coupon.getCouponCode());
            statement.setString(2, coupon.getDescription());
            statement.setString(3, coupon.getDiscountType());
            statement.setBigDecimal(4, coupon.getDiscountValue());
            statement.setBigDecimal(5, coupon.getMinimumOrderAmount());

            setLocalDateTime(statement, 6, coupon.getStartDate());
            setLocalDateTime(statement, 7, coupon.getEndDate());

            statement.setInt(8, coupon.getUsageLimit());
            statement.setString(9, coupon.getStatus());
            statement.setInt(10, coupon.getCouponId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateStatus(
            int couponId,
            String status) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_STATUS_SQL)) {

            statement.setString(1, status);
            statement.setInt(2, couponId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateDiscountValue(
            int couponId,
            BigDecimal discountValue) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_DISCOUNT_VALUE_SQL)) {

            statement.setBigDecimal(
                    1,
                    discountValue);

            statement.setInt(
                    2,
                    couponId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateUsageLimit(
            int couponId,
            int usageLimit) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_USAGE_LIMIT_SQL)) {

            statement.setInt(1, usageLimit);
            statement.setInt(2, couponId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateDates(
            int couponId,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_DATES_SQL)) {

            setLocalDateTime(
                    statement,
                    1,
                    startDate);

            setLocalDateTime(
                    statement,
                    2,
                    endDate);

            statement.setInt(
                    3,
                    couponId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public boolean deleteCoupon(int couponId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_COUPON_SQL)) {

            statement.setInt(1, couponId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =====================================================
    // COUNT
    // =====================================================

    @Override
    public int countAll() {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             COUNT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    public int countByStatus(String status) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             COUNT_BY_STATUS_SQL)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // =====================================================
    // RESULT-SET MAPPING
    // =====================================================

    private Coupon mapResultSetToCoupon(
            ResultSet resultSet)
            throws SQLException {

        Coupon coupon = new Coupon();

        coupon.setCouponId(
                resultSet.getInt("coupon_id"));

        coupon.setCouponCode(
                resultSet.getString("coupon_code"));

        coupon.setDescription(
                resultSet.getString("description"));

        coupon.setDiscountType(
                resultSet.getString("discount_type"));

        coupon.setDiscountValue(
                resultSet.getBigDecimal(
                        "discount_value"));

        coupon.setMinimumOrderAmount(
                resultSet.getBigDecimal(
                        "minimum_order_amount"));

        Timestamp startTimestamp =
                resultSet.getTimestamp("start_date");

        if (startTimestamp != null) {
            coupon.setStartDate(
                    startTimestamp.toLocalDateTime());
        }

        Timestamp endTimestamp =
                resultSet.getTimestamp("end_date");

        if (endTimestamp != null) {
            coupon.setEndDate(
                    endTimestamp.toLocalDateTime());
        }

        coupon.setUsageLimit(
                resultSet.getInt("usage_limit"));

        coupon.setStatus(
                resultSet.getString("status"));

        return coupon;
    }


    // =====================================================
    // DATE HELPER
    // =====================================================

    private void setLocalDateTime(
            PreparedStatement statement,
            int parameterIndex,
            LocalDateTime value)
            throws SQLException {

        if (value == null) {
            statement.setTimestamp(
                    parameterIndex,
                    null);
        } else {
            statement.setTimestamp(
                    parameterIndex,
                    Timestamp.valueOf(value));
        }
    }
}
