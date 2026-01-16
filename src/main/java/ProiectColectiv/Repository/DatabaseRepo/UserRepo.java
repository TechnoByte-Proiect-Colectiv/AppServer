package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.Address;
import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
            return null;
        }
    }

    // Helper method to fetch addresses for a user
    private List<Address> getAddressesForUser(Connection con, String userEmail) {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM Addresses WHERE userEmail=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address addr = new Address(
                            rs.getString("id"),
                            rs.getString("type"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("county"),
                            rs.getString("postalCode"),
                            rs.getString("country"),
                            rs.getString("phoneNumber"),
                            rs.getBoolean("isPrimary")
                    );
                    addresses.add(addr);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching addresses: " + e);
        }
        return addresses;
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
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    String authToken = rs.getString("authToken");
                    String phoneNumber = rs.getString("phoneNumber");
                    LocalDateTime lastLogin = parseLocalDateTime(rs.getString("lastLogin"));

                    Date sqlDateCreated = rs.getDate("dateCreated");
                    LocalDate dateCreated = (sqlDateCreated != null) ? sqlDateCreated.toLocalDate() : null;

                    User user = new User(firstName, lastName, email, password, isAdmin, authToken, lastLogin, dateCreated, phoneNumber);

                    user.setAddresses(getAddressesForUser(con, email));

                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById: " + e);
        }
        return null;
    }

    public void addAddress(String userEmail, Address address) {
        Connection con = dbUtils.getConnection();
        String query = "INSERT INTO Addresses(id, userEmail, type, firstName, lastName, street, city, county, postalCode, country, phoneNumber, isPrimary) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        if (address.isPrimary()) {
            removePrimaryStatus(userEmail);
        }

        try (PreparedStatement ps = con.prepareStatement(query)) {
            String addrId = (address.getId() == null || address.getId().isEmpty()) ? UUID.randomUUID().toString() : address.getId();

            ps.setString(1, addrId);
            ps.setString(2, userEmail);
            ps.setString(3, address.getType());
            ps.setString(4, address.getFirstName());
            ps.setString(5, address.getLastName());
            ps.setString(6, address.getStreet());
            ps.setString(7, address.getCity());
            ps.setString(8, address.getCounty());
            ps.setString(9, address.getPostalCode());
            ps.setString(10, address.getCountry());
            ps.setString(11, address.getPhoneNumber());
            ps.setBoolean(12, address.isPrimary());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding address: " + e.getMessage());
        }
    }

    @Override
    public void save(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "INSERT INTO Users(firstName, lastName, email, password, isAdmin, authToken, lastLogin, dateCreated, phoneNumber) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setBoolean(5, entity.isAdmin());
            ps.setString(6, entity.getAuthToken());

            if(entity.getLastLogin() != null) {
                ps.setObject(7,Timestamp.valueOf(entity.getLastLogin()));
            }
            else{
                ps.setObject(7,null);
            }
            ps.setDate(8, Date.valueOf(entity.getDateCreated()));
            ps.setString(9, entity.getPhoneNumber());
          
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB save: " + e.getMessage());
        }
    }

    private void removePrimaryStatus(String userEmail) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Addresses SET isPrimary=0 WHERE userEmail=?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, userEmail);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing primary status: " + e);
        }
    }

    public void deleteAddress(String addressId) {
        Connection con = dbUtils.getConnection();
        String query = "DELETE FROM Addresses WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, addressId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting address: " + e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Users SET firstName=?, lastName=?, password=?, isAdmin=?, authToken=?, lastLogin=?, dateCreated=?, phoneNumber=? WHERE email=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getPassword());
            ps.setBoolean(4, entity.isAdmin());
            ps.setString(5, entity.getAuthToken());
          
            if(entity.getLastLogin() != null) ps.setObject(6,Timestamp.valueOf(entity.getLastLogin())); else ps.setObject(6,null);
            ps.setDate(7, Date.valueOf(entity.getDateCreated()));
            ps.setString(8, entity.getPhoneNumber());
            ps.setString(9, entity.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update: " + e.getMessage());
        }
    }

    public void updatePassword(User entity) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Users SET password=? WHERE email=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity.getPassword());
            ps.setString(2, entity.getEmail());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB update: " + e.getMessage());
        }
    }

    public User findByToken(String token) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Users WHERE authToken=?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, token);

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    String authToken = rs.getString("authToken");
                    String phoneNumber = rs.getString("phoneNumber");
                    LocalDateTime lastLogin = parseLocalDateTime(rs.getString("lastLogin"));

                    Date sqlDateCreated = rs.getDate("dateCreated");
                    LocalDate dateCreated = (sqlDateCreated != null) ? sqlDateCreated.toLocalDate() : null;

                    User user = new User(firstName, lastName, email, password, isAdmin, authToken, lastLogin, dateCreated, phoneNumber);

                    user.setAddresses(getAddressesForUser(con, user.getEmail()));

                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findByToken: " + e);
        }
        return null;
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
                    String phoneNumber = rs.getString("phoneNumber");

                    Timestamp timestampLogin = rs.getTimestamp("lastLogin");
                    LocalDateTime lastLogin = (timestampLogin != null) ? timestampLogin.toLocalDateTime() : null;

                    Date sqlDateCreated = rs.getDate("dateCreated");
                    LocalDate dateCreated = (sqlDateCreated != null) ? sqlDateCreated.toLocalDate() : null;

                    User user = new User(firstName, lastName, email, password, isAdmin, authToken, lastLogin, dateCreated,phoneNumber);

                    user.setAddresses(getAddressesForUser(con, user.getEmail()));

                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            System.err.println("Error DB getAllUsers: " + e);
        }
        return users;
    }

    public void updateAddress(Address address) {
        Connection con = dbUtils.getConnection();
        String query = "UPDATE Addresses SET type=?, firstName=?, lastName=?, street=?, city=?, county=?, postalCode=?, country=?, phoneNumber=?, isPrimary=? WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, address.getType());
            ps.setString(2, address.getFirstName());
            ps.setString(3, address.getLastName());
            ps.setString(4, address.getStreet());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getCounty());
            ps.setString(7, address.getPostalCode());
            ps.setString(8, address.getCountry());
            ps.setString(9, address.getPhoneNumber());
            ps.setBoolean(10, address.isPrimary());
            ps.setString(11, address.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating address: " + e.getMessage());
        }
    }
}
