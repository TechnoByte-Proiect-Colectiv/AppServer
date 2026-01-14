package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.CartItem;
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
    public Order findById(Integer orderId) {
        Connection con = dbUtils.getConnection();
        // Selectam tot, dar maparea se face jos in 'extractOrderFromResultSet'
        String query = "SELECT * FROM Orders WHERE idOrder = ?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setInt(1, orderId);

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
        // Am adaugat coloanele noi: currency, billingAddress, shippingAddress
        // Am scos: address
        String query = "INSERT INTO Orders (idUser, orderDate, totalProducts, totalShipping, totalPrice, currency, paymentMethod, paymentStatus, deliveryStatus, billingAddress, shippingAddress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, entity.getUserID());

            // Convertim LocalDate in String pentru SQLite
            ps.setString(2, entity.getOrderDate() != null ? entity.getOrderDate().toString() : null);

            ps.setObject(3, entity.getTotalProducts()); // setObject gestioneaza mai bine null-urile decat setFloat
            ps.setObject(4, entity.getTotalShipping());
            ps.setObject(5, entity.getTotalPrice());
            ps.setString(6, entity.getCurrency());
            ps.setString(7, entity.getPaymentMethod());
            ps.setObject(8, entity.getPaymentStatus());
            ps.setString(9, entity.getDeliveryStatus());
            ps.setString(10, entity.getBillingAddress());
            ps.setString(11, entity.getShippingAddress());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save Order: " + e.getMessage());
        }
    }

    @Override
    public void update(Order entity) {
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE Orders SET orderDate=?, totalProducts=?, totalShipping=?, totalPrice=?, currency=?, paymentMethod=?, paymentStatus=?, deliveryStatus=?, billingAddress=?, shippingAddress=? WHERE idOrder=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, entity.getOrderDate() != null ? entity.getOrderDate().toString() : null);
            ps.setObject(2, entity.getTotalProducts());
            ps.setObject(3, entity.getTotalShipping());
            ps.setObject(4, entity.getTotalPrice());
            ps.setString(5, entity.getCurrency());
            ps.setString(6, entity.getPaymentMethod());
            ps.setObject(7, entity.getPaymentStatus());
            ps.setString(8, entity.getDeliveryStatus());
            ps.setString(9, entity.getBillingAddress());
            ps.setString(10, entity.getShippingAddress());

            // WHERE clause
            ps.setInt(11, entity.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update Order: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer orderId) {
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM Orders WHERE idOrder = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete Order: " + e.getMessage());
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        // Parsare data din String (SQLite) in LocalDate
        String dateString = rs.getString("orderDate");
        LocalDate orderDate = (dateString != null) ? LocalDate.parse(dateString) : null;

        // Extragere valori
        Integer orderId = rs.getInt("idOrder");
        String userID = rs.getString("idUser");
        Float totalProducts = rs.getObject("totalProducts", Float.class);
        Float totalShipping = rs.getObject("totalShipping", Float.class);
        Float totalPrice = rs.getObject("totalPrice", Float.class);
        String currency = rs.getString("currency");
        String paymentMethod = rs.getString("paymentMethod");
        Boolean paymentStatus = rs.getObject("paymentStatus", Boolean.class);
        String deliveryStatus = rs.getString("deliveryStatus");
        String billingAddress = rs.getString("billingAddress");
        String shippingAddress = rs.getString("shippingAddress");

        return new Order(orderId,userID, orderDate, totalProducts, totalShipping, totalPrice, currency,
                paymentMethod, paymentStatus, deliveryStatus, billingAddress, shippingAddress);
    }

    @Override
    public Iterable<Order> getAllOrdersByUser(String userId) {
        List<Order> items = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        String query = "SELECT * FROM CartItems WHERE idUser = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String dateString = rs.getString("orderDate");
                    LocalDate orderDate = (dateString != null) ? LocalDate.parse(dateString) : null;
                    Integer orderId = rs.getInt("idOrder");
                    Float totalProducts = rs.getObject("totalProducts", Float.class);
                    Float totalShipping = rs.getObject("totalShipping", Float.class);
                    Float totalPrice = rs.getObject("totalPrice", Float.class);
                    String currency = rs.getString("currency");
                    String paymentMethod = rs.getString("paymentMethod");
                    Boolean paymentStatus = rs.getObject("paymentStatus", Boolean.class);
                    String deliveryStatus = rs.getString("deliveryStatus");
                    String billingAddress = rs.getString("billingAddress");
                    String shippingAddress = rs.getString("shippingAddress");

                    items.add(new Order(orderId,userId,orderDate,totalProducts,totalShipping,totalPrice,currency,paymentMethod,paymentStatus,deliveryStatus,billingAddress,shippingAddress));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findAllForOrder: " + e);
        }

        return items;
    }
}