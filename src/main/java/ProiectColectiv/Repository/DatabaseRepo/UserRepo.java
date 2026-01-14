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

    private LocalDateTime parseLocalDateTime(String str) {
        if (str == null || str.isEmpty()) return null;
        try {
            return LocalDateTime.parse(str.replace(" ", "T"));
        } catch (Exception e) {
            try {
                long millis = Long.parseLong(str);
                return LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(millis), java.time.ZoneId.systemDefault());
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private LocalDate parseLocalDate(String str) {
        if (str == null || str.isEmpty()) return null;
        try {
            if (str.length() > 10) {
                return LocalDate.parse(str.substring(0, 10));
            }
            return LocalDate.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBoolean("isAdmin"),
                rs.getString("authToken"),
                parseLocalDateTime(rs.getString("lastLogin")),
                rs.getString("address"),
                parseLocalDate(rs.getString("dateCreated")),
                rs.getString("phoneNumber")
        );
    }

    @Override
    public User findById(String id) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Users WHERE email=?";
        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, id);
            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
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
        String query = "INSERT INTO Users(firstName, lastName, email, password, isAdmin, authToken, lastLogin, address, dateCreated, phoneNumber) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setBoolean(5, entity.isAdmin());
            ps.setString(6, entity.getAuthToken());
            ps.setString(7, entity.getLastLogin() != null ? entity.getLastLogin().toString() : null);
            ps.setString(8, entity.getAddress());
            ps.setString(9, entity.getDateCreated() != null ? entity.getDateCreated().toString() : LocalDate.now().toString());
            ps.setString(10, entity.getPhoneNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save: " + e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Users SET firstName=?, lastName=?, password=?, isAdmin=?, authToken=?, lastLogin=?, address=?, dateCreated=?, phoneNumber=? WHERE email=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setBoolean(4, entity.isAdmin());
            ps.setString(5, entity.getAuthToken());
            ps.setString(6, entity.getLastLogin() != null ? entity.getLastLogin().toString() : null);
            ps.setString(7, entity.getAddress());
            ps.setString(8, entity.getDateCreated() != null ? entity.getDateCreated().toString() : null);
            ps.setString(9, entity.getPhoneNumber());
            ps.setString(10, entity.getEmail());
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
        try (PreparedStatement preStmt = con.prepareStatement(query);
             ResultSet rs = preStmt.executeQuery()) {
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            return users;
        } catch (SQLException e) {
            System.err.println("Error DB getAllUsers: " + e);
        }
        return users;
    }

    @Override
    public void updatePassword(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Users SET password=? WHERE email=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getPassword());
            ps.setString(2, entity.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB updatePassword: " + e.getMessage());
        }
    }
}