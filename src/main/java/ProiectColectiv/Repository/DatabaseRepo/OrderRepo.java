package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Order;
import ProiectColectiv.Repository.Interfaces.ICartItemRepo;
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
        String query = "SELECT * FROM Orders WHERE idUser = ?";

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
        String query = "INSERT INTO Orders (idUser, orderDate, totalProducts, totalShipping, totalPrice, paymentMethod, paymentStatus, deliveryStatus, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, entity.getUserID());
            ps.setObject(2, Date.valueOf(entity.getOrderDate()));
            ps.setFloat(3, entity.getTotalProducts());
            ps.setFloat(4, entity.getTotalShipping());
            ps.setFloat(5, entity.getTotalPrice());
            ps.setString(6, entity.getPaymentMethod());
            ps.setBoolean(7, entity.getPaymentStatus());
            ps.setString(8, entity.getDeliveryStatus());
            ps.setString(9, entity.getAddress());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save Order: " + e.getMessage());
        }
    }

    @Override
    public void update(Order entity) {
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE Orders SET orderDate=?, totalProducts=?, totalShipping=?, totalPrice=?, paymentMethod=?, paymentStatus=?, deliveryStatus=?, address=? WHERE idUser=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setObject(1, entity.getOrderDate());
            ps.setFloat(2, entity.getTotalProducts());
            ps.setFloat(3, entity.getTotalShipping());
            ps.setFloat(4, entity.getTotalPrice());
            ps.setString(5, entity.getPaymentMethod());
            ps.setBoolean(6, entity.getPaymentStatus());
            ps.setString(7, entity.getDeliveryStatus());
            ps.setString(8, entity.getAddress());
            ps.setString(9, entity.getUserID());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update Order: " + e.getMessage());
        }
    }

    @Override
    public void delete(String userId) {
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM Orders WHERE idUser = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete Order: " + e.getMessage());
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        String userID = rs.getString("idUser");
        Date sqlDate = rs.getDate("orderDate");
        LocalDate orderDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        Float totalProducts = rs.getFloat("totalProducts");
        Float totalShipping = rs.getFloat("totalShipping");
        Float totalPrice = rs.getFloat("totalPrice");
        String paymentMethod = rs.getString("paymentMethod");
        Boolean paymentStatus = rs.getBoolean("paymentStatus");
        String deliveryStatus = rs.getString("deliveryStatus");
        String address = rs.getString("address");

        return new Order(userID, orderDate, totalProducts, totalShipping, totalPrice, paymentMethod, paymentStatus, deliveryStatus, address);
    }
}