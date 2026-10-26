package com.zoloto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zoloto.model.Address;
import com.zoloto.util.DBConnection;

public class AddressDAOImpl implements AddressDAO {

    private static final String INSERT_ADDRESS_SQL =
            "INSERT INTO addresses "
            + "(user_id, full_name, mobile, address_line1, address_line2, "
            + "city, state, country, pincode, address_type, is_default) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT address_id, user_id, full_name, mobile, address_line1, "
            + "address_line2, city, state, country, pincode, address_type, is_default "
            + "FROM addresses WHERE address_id = ?";

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT address_id, user_id, full_name, mobile, address_line1, "
            + "address_line2, city, state, country, pincode, address_type, is_default "
            + "FROM addresses WHERE user_id = ? ORDER BY address_id";

    private static final String FIND_BY_USER_AND_TYPE_SQL =
            "SELECT address_id, user_id, full_name, mobile, address_line1, "
            + "address_line2, city, state, country, pincode, address_type, is_default "
            + "FROM addresses "
            + "WHERE user_id = ? AND address_type = ? "
            + "ORDER BY address_id";

    private static final String FIND_DEFAULT_ADDRESS_SQL =
            "SELECT address_id, user_id, full_name, mobile, address_line1, "
            + "address_line2, city, state, country, pincode, address_type, is_default "
            + "FROM addresses "
            + "WHERE user_id = ? AND is_default = true "
            + "LIMIT 1";

    private static final String FIND_ALL_SQL =
            "SELECT address_id, user_id, full_name, mobile, address_line1, "
            + "address_line2, city, state, country, pincode, address_type, is_default "
            + "FROM addresses ORDER BY address_id";

    private static final String EXISTS_BY_ID_SQL =
            "SELECT COUNT(*) FROM addresses WHERE address_id = ?";

    private static final String COUNT_BY_USER_ID_SQL =
            "SELECT COUNT(*) FROM addresses WHERE user_id = ?";

    private static final String UPDATE_ADDRESS_SQL =
            "UPDATE addresses SET "
            + "full_name = ?, "
            + "mobile = ?, "
            + "address_line1 = ?, "
            + "address_line2 = ?, "
            + "city = ?, "
            + "state = ?, "
            + "country = ?, "
            + "pincode = ?, "
            + "address_type = ?, "
            + "is_default = ? "
            + "WHERE address_id = ?";

    private static final String UPDATE_ADDRESS_TYPE_SQL =
            "UPDATE addresses SET address_type = ? "
            + "WHERE address_id = ?";

    private static final String CLEAR_DEFAULT_ADDRESS_SQL =
            "UPDATE addresses SET is_default = false "
            + "WHERE user_id = ?";

    private static final String SET_DEFAULT_ADDRESS_SQL =
            "UPDATE addresses SET is_default = true "
            + "WHERE address_id = ? AND user_id = ?";

    private static final String DELETE_ADDRESS_SQL =
            "DELETE FROM addresses WHERE address_id = ?";

    private static final String DELETE_ALL_BY_USER_ID_SQL =
            "DELETE FROM addresses WHERE user_id = ?";


    @Override
    public boolean createAddress(Address address) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_ADDRESS_SQL)) {

            statement.setInt(1, address.getUserId());
            statement.setString(2, address.getFullName());
            statement.setString(3, address.getMobile());
            statement.setString(4, address.getAddressLine1());
            statement.setString(5, address.getAddressLine2());
            statement.setString(6, address.getCity());
            statement.setString(7, address.getState());
            statement.setString(8, address.getCountry());
            statement.setString(9, address.getPincode());
            statement.setString(10, address.getAddressType());
            statement.setBoolean(11, address.isDefaultAddress());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Address findById(int addressId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, addressId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToAddress(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Address> findByUserId(int userId) {

        List<Address> addresses = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    addresses.add(mapResultSetToAddress(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addresses;
    }


    @Override
    public List<Address> findByUserIdAndType(
            int userId,
            String addressType) {

        List<Address> addresses = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_USER_AND_TYPE_SQL)) {

            statement.setInt(1, userId);
            statement.setString(2, addressType);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    addresses.add(mapResultSetToAddress(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addresses;
    }


    @Override
    public Address findDefaultAddressByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_DEFAULT_ADDRESS_SQL)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToAddress(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Address> findAll() {

        List<Address> addresses = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                addresses.add(mapResultSetToAddress(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addresses;
    }


    @Override
    public boolean existsById(int addressId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(EXISTS_BY_ID_SQL)) {

            statement.setInt(1, addressId);

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
    public int countByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);

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


    @Override
    public boolean updateAddress(Address address) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_ADDRESS_SQL)) {

            statement.setString(1, address.getFullName());
            statement.setString(2, address.getMobile());
            statement.setString(3, address.getAddressLine1());
            statement.setString(4, address.getAddressLine2());
            statement.setString(5, address.getCity());
            statement.setString(6, address.getState());
            statement.setString(7, address.getCountry());
            statement.setString(8, address.getPincode());
            statement.setString(9, address.getAddressType());
            statement.setBoolean(10, address.isDefaultAddress());
            statement.setInt(11, address.getAddressId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateAddressType(
            int addressId,
            String addressType) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_ADDRESS_TYPE_SQL)) {

            statement.setString(1, addressType);
            statement.setInt(2, addressId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean setDefaultAddress(
            int userId,
            int addressId) {

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement clearStatement =
                         connection.prepareStatement(
                                 CLEAR_DEFAULT_ADDRESS_SQL);
                 PreparedStatement setStatement =
                         connection.prepareStatement(
                                 SET_DEFAULT_ADDRESS_SQL)) {

                clearStatement.setInt(1, userId);
                clearStatement.executeUpdate();

                setStatement.setInt(1, addressId);
                setStatement.setInt(2, userId);

                int rowsUpdated =
                        setStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    connection.commit();
                    return true;
                }

                connection.rollback();
                return false;

            } catch (SQLException e) {

                connection.rollback();
                throw e;

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public boolean clearDefaultAddress(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             CLEAR_DEFAULT_ADDRESS_SQL)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteAddress(int addressId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_ADDRESS_SQL)) {

            statement.setInt(1, addressId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteAllByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_ALL_BY_USER_ID_SQL)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private Address mapResultSetToAddress(
            ResultSet resultSet)
            throws SQLException {

        Address address = new Address();

        address.setAddressId(
                resultSet.getInt("address_id"));

        address.setUserId(
                resultSet.getInt("user_id"));

        address.setFullName(
                resultSet.getString("full_name"));

        address.setMobile(
                resultSet.getString("mobile"));

        address.setAddressLine1(
                resultSet.getString("address_line1"));

        address.setAddressLine2(
                resultSet.getString("address_line2"));

        address.setCity(
                resultSet.getString("city"));

        address.setState(
                resultSet.getString("state"));

        address.setCountry(
                resultSet.getString("country"));

        address.setPincode(
                resultSet.getString("pincode"));

        address.setAddressType(
                resultSet.getString("address_type"));

        address.setDefaultAddress(
                resultSet.getBoolean("is_default"));

        return address;
    }
}