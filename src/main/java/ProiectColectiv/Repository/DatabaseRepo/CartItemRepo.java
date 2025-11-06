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
    public CartItem findById(CompositeKey<String,Integer> compositeKey) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from CartItem WHERE userID = ? AND productID = ?")) {

            /// aici este mai sus :))
            preStmt.setString(1, compositeKey.key1()); // key1 este userID (String)
            preStmt.setInt(2, compositeKey.key2());   // key2 este productID (Integer)

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    String userID = rs.getString("userID");
                    Integer productID = rs.getInt("productID");
                    Integer nrOrdered = rs.getInt("nrOrdered");

                    /// nu mai trebuie setID daca am facut mai sus setarea la compositeKey
                    return new CartItem(userID, productID, nrOrdered);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public void save(CartItem entity) {
            Connection conn = dbUtils.getConnection();

            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO CartItem (userID, productID, nrOrdered) VALUES (?, ?, ?)")) {

                ps.setString(1, entity.getIdUser());   // key1
                ps.setInt(2, entity.getIdProduct());   // key2
                ps.setInt(3, entity.getNrOrdered());

                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e.getMessage());
            }
    }

    @Override
    public void update(CartItem entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE CartItems SET nrOrdered = ? WHERE userID = ? AND productID = ?")) {
            /// 1. Setează câmpurile de actualizat (non-cheie)
            ps.setInt(1, entity.getNrOrdered());

            /// 2. Setează câmpurile cheie pentru clauza WHERE
            ps.setString(2, entity.getIdUser()); // key1
            ps.setInt(3, entity.getIdProduct()); // key2

            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("Error DB " + e.getMessage());
        }
    }

    @Override
    public void delete(CompositeKey<String, Integer> compositeKey) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM CartItem WHERE userID = ? AND productID = ?")) {

            ps.setString(1, compositeKey.key1()); // userID
            ps.setInt(2, compositeKey.key2());   // productID

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB " + e.getMessage());
        }
    }

    @Override
    public Iterable<CartItem> findAllForUser(String userID) {
        List<CartItem> items = new ArrayList<>(); // lista goala
        Connection con = dbUtils.getConnection();

        // caut TOATE intrările pentru un userID
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM CartItem WHERE userID = ?")) {

            ps.setString(1, userID); // setez parametrul userID

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // datele pentru rândul curent
                    String currentUserID = rs.getString("userID");
                    Integer productID = rs.getInt("productID");
                    Integer nrOrdered = rs.getInt("nrOrdered");

                    // Creăm obiectul și îl adăugăm în listă
                    items.add(new CartItem(currentUserID, productID, nrOrdered));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }

        return items;
    }

    @Override
    public void clearCartForUser(String userID) {
        Connection conn = dbUtils.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM CartItem WHERE userID = ?")) {

            ps.setString(1, userID); // setez parametrul userID
            ps.executeUpdate(); // execut ștergerea

        } catch (SQLException e) {
            System.err.println("Error DB " + e.getMessage());
        }
    }
}
