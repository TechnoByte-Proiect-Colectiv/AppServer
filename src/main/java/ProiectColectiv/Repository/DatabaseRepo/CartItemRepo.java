package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.CartItem;
import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Repository.Interfaces.ICartItemRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CartItemRepo implements ICartItemRepo {
    private final JdbcUtils dbUtils;

    public CartItemRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public CartItem findById(CompositeKey<String, Integer> compositeKey) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM CartItem WHERE idOrder = ? AND productID = ?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, compositeKey.key1()); // key1 este idOrder (String)
            preStmt.setInt(2, compositeKey.key2());   // key2 este productID (Integer)

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    String idOrder = rs.getString("idOrder");
                    Integer productID = rs.getInt("productID");
                    Integer nrOrdered = rs.getInt("nrOrdered");

                    return new CartItem(idOrder, productID, nrOrdered);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById CartItem: " + e);
        }
        return null;
    }

    @Override
    public void save(CartItem entity) {
        Connection conn = dbUtils.getConnection();
        String query = "INSERT INTO CartItem (idOrder, productID, nrOrdered) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, entity.getOrderID());
            ps.setInt(2, entity.getProductID());
            ps.setInt(3, entity.getNrOrdered());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save CartItem: " + e.getMessage());
        }
    }

    @Override
    public void update(CartItem entity) {
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE CartItem SET nrOrdered = ? WHERE idOrder = ? AND productID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, entity.getNrOrdered());

            ps.setString(2, entity.getOrderID());   // key1 -> idOrder
            ps.setInt(3, entity.getProductID());    // key2 -> productID

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update CartItem: " + e.getMessage());
        }
    }

    @Override
    public void delete(CompositeKey<String, Integer> compositeKey) {
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM CartItem WHERE idOrder = ? AND productID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, compositeKey.key1()); // idOrder
            ps.setInt(2, compositeKey.key2());   // productID

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete CartItem: " + e.getMessage());
        }
    }

    @Override
    public Iterable<CartItem> findAllForUser(String idOrder) {
        List<CartItem> items = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        String query = "SELECT * FROM CartItem WHERE idOrder = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, idOrder);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String currentIdOrder = rs.getString("idOrder");
                    Integer productID = rs.getInt("productID");
                    Integer nrOrdered = rs.getInt("nrOrdered");

                    items.add(new CartItem(currentIdOrder, productID, nrOrdered));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findAllForOrder: " + e);
        }

        return items;
    }

    @Override
    public void clearCartForUser(String idOrder) {
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM CartItem WHERE idOrder = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, idOrder);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB clearCart: " + e.getMessage());
        }
    }
}