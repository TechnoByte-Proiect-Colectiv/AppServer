package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Order;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrderRepo implements IOrderRepo {
    private final JdbcUtils dbUtils;

    public OrderRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Order findById(String userId) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Orders WHERE userID = ?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, userId); // userID

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    return extractOrderFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById Order: " + e);
        }
        return null;
    }

    @Override
    public void save(Order entity) {
        Connection conn = dbUtils.getConnection();
        String query = "INSERT INTO Orders (userID, quantity, orderDate, totalProducts, totalShipping, totalPrice, paymentMethod, paymentStatus, deliveryStatus, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, entity.getUserID());
            ps.setInt(3, entity.getQuantity());
            ps.setObject(4, entity.getOrderDate());
            ps.setFloat(5, entity.getTotalProducts());
            ps.setFloat(6, entity.getTotalShipping());
            ps.setFloat(7, entity.getTotalPrice());
            ps.setString(8, entity.getPaymentMethod());
            ps.setBoolean(9, entity.getPaymentStatus());
            ps.setString(10, entity.getDeliveryStatus());
            ps.setString(11, entity.getAddress());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save Order: " + e.getMessage());
        }
    }

    @Override
    public void update(Order entity) {
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE Orders SET quantity=?, orderDate=?, totalProducts=?, totalShipping=?, totalPrice=?, paymentMethod=?, paymentStatus=?, deliveryStatus=?, address=? WHERE userID=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, entity.getQuantity());
            ps.setObject(2, entity.getOrderDate());
            ps.setFloat(3, entity.getTotalProducts());
            ps.setFloat(4, entity.getTotalShipping());
            ps.setFloat(5, entity.getTotalPrice());
            ps.setString(6, entity.getPaymentMethod());
            ps.setBoolean(7, entity.getPaymentStatus());
            ps.setString(8, entity.getDeliveryStatus());
            ps.setString(9, entity.getAddress());
            ps.setString(10, entity.getUserID());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update Order: " + e.getMessage());
        }
    }

    @Override
    public void delete(String userId) {
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM Orders WHERE userID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete Order: " + e.getMessage());
        }
    }

    @Override
    public Iterable<Order> findAllForUser(String userID) {
        List<Order> items = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        String query = "SELECT * FROM Orders WHERE userID = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(extractOrderFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findAllForUser Order: " + e);
        }

        return items;
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        String userID = rs.getString("userID");
        Integer quantity = rs.getInt("quantity");
        Date sqlDate = rs.getDate("orderDate");
        LocalDate orderDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        Float totalProducts = rs.getFloat("totalProducts");
        Float totalShipping = rs.getFloat("totalShipping");
        Float totalPrice = rs.getFloat("totalPrice");
        String paymentMethod = rs.getString("paymentMethod");
        Boolean paymentStatus = rs.getBoolean("paymentStatus");
        String deliveryStatus = rs.getString("deliveryStatus");
        String address = rs.getString("address");

        return new Order(userID, quantity, orderDate, totalProducts, totalShipping, totalPrice, paymentMethod, paymentStatus, deliveryStatus, address);
    }
}