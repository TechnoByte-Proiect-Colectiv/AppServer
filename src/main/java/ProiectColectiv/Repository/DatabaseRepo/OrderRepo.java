package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Order;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class OrderRepo implements IOrderRepo {
    private final JdbcUtils dbUtils;

    public OrderRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Order findById(CompositeKey<String,Integer> compositeKey) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Orders where id=?")){

            /// aici este mai sus :))
            preStmt.setString(1, compositeKey.key1()); // key1 este userID (String)
            preStmt.setInt(2, compositeKey.key2());   // key2 este productID (Integer)

            preStmt.setObject(1,compositeKey);
            try (ResultSet rs=preStmt.executeQuery()){
                if(rs.next())
                {
                    String userID = rs.getString("userID");
                    Integer productID = rs.getInt("productID");
                    Integer quantity = rs.getInt("quantity");
                    String deliveryStatus = rs.getString("deliveryStatus");

                    /// nu mai trebuie setID daca am facut mai sus setarea la compositeKey
                    return new Order(userID,productID,quantity,deliveryStatus);
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return null;
    }

    @Override
    public void update(Order entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Orders SET quantity = ?, deliveryStatus = ? WHERE userID = ? AND productID = ?")) {
            /// 1. Setează câmpurile de actualizat (non-cheie)
            ps.setInt(1, entity.getQuantity());
            ps.setString(2, entity.getDeliveryStatus());

            /// 2. Setează câmpurile cheie pentru clauza WHERE
            ps.setString(3, entity.getUserID()); // key1
            ps.setInt(4, entity.getProductID()); // key2

            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("Error DB " + e.getMessage());
        }
    }

    @Override
    public void delete(CompositeKey<String, Integer> compositeKey) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Orders WHERE userID = ? AND productID = ?")) {

            ps.setString(1, compositeKey.key1()); // userID
            ps.setInt(2, compositeKey.key2());   // productID

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB " + e.getMessage());
        }
    }

    @Override
    public void save(Order entity) {
        Connection conn = dbUtils.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Orders (userID, productID, quantity, deliveryStatus) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, entity.getUserID());   // key1
            ps.setInt(2, entity.getProductID());   // key2
            ps.setInt(3, entity.getQuantity());
            ps.setString(4, entity.getDeliveryStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB " + e.getMessage());
        }
    }
}
