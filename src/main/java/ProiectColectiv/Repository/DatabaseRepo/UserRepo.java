package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class UserRepo implements IUserRepo {
    private final JdbcUtils dbUtils;

    public UserRepo(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User findById(String id) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Users WHERE id=?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, id);

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    String extractedId = rs.getString("id");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    String authToken = rs.getString("authToken");
                    String adress = rs.getString("address");

                    Timestamp timestampLogin = rs.getTimestamp("lastLogin");
                    LocalDateTime lastLogin = (timestampLogin != null) ? timestampLogin.toLocalDateTime() : null;

                    Date sqlDateCreated = rs.getDate("dateCreated");
                    LocalDate dateCreated = (sqlDateCreated != null) ? sqlDateCreated.toLocalDate() : null;

                    User user = new User(firstName, lastName, email, password, isAdmin, authToken, lastLogin, adress, dateCreated);
                    user.setId(extractedId);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById: " + e);
        }
        return null;
    }

    @Override
    public void save(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "INSERT INTO Users(id, firstName, lastName, email, password, isAdmin, authToken, lastLogin, address, dateCreated) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getPassword());
            ps.setBoolean(6, entity.isAdmin());
            ps.setString(7, entity.getAuthToken());

            ps.setObject(8, entity.getLastLogin());
            ps.setString(9, entity.getAddress());
            ps.setObject(10, entity.getDateCreated());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save: " + e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Users SET firstName=?, lastName=?, email=?, password=?, isAdmin=?, authToken=?, lastLogin=?, address=?, dateCreated=? WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setBoolean(5, entity.isAdmin());
            ps.setString(6, entity.getAuthToken());
            ps.setObject(7, entity.getLastLogin());
            ps.setString(8, entity.getAddress());
            ps.setObject(9, entity.getDateCreated());
            ps.setString(10, entity.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        Connection con = dbUtils.getConnection();
        String query = "DELETE FROM Users WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete: " + e.getMessage());
        }
    }
}