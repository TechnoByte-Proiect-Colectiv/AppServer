package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;

public class UserRepo implements IUserRepo {
    private final JdbcUtils dbUtils;

    public UserRepo(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User findById(String id) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Users WHERE email=?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, id);

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
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
        String query = "INSERT INTO Users(firstName, lastName, email, password, isAdmin, authToken, lastLogin, address, dateCreated) VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setBoolean(5, entity.isAdmin());
            ps.setString(6, entity.getAuthToken());

            ps.setObject(7, entity.getLastLogin());
            ps.setString(8, entity.getAddress());
            ps.setDate(9, Date.valueOf(entity.getDateCreated()));

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save: " + e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Users SET firstName=?, lastName=?, password=?, isAdmin=?, authToken=?, lastLogin=?, address=?, dateCreated=? WHERE email=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setBoolean(4, entity.isAdmin());
            ps.setString(5, entity.getAuthToken());
            ps.setObject(6, entity.getLastLogin());
            ps.setString(7, entity.getAddress());
            ps.setDate(8, Date.valueOf(entity.getDateCreated()));
            ps.setString(9, entity.getEmail());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        Connection con = dbUtils.getConnection();
        String query = "DELETE FROM Users WHERE email=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete: " + e.getMessage());
        }
    }

    @Override
    public Iterable<User> getAllUsers() {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Users";
        ArrayList<User> users = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
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
                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            System.err.println("Error DB getAllUsers: " + e);
        }
        return null;
    }
}