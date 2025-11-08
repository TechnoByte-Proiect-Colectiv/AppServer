package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class UserRepo implements IUserRepo {
    private final JdbcUtils dbUtils;

    public UserRepo(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User findById(String s) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Products where id=?")){
            preStmt.setString(1,s);
            try(ResultSet rs=preStmt.executeQuery()){
                String id = rs.getString("id");
                String password = rs.getString("password");
                Boolean isAdmin = rs.getBoolean("isAdmin");
                String authToken = rs.getString("authToken");
                LocalDateTime lastLogin = rs.getTimestamp("lastLogin").toLocalDateTime();
                String adress = rs.getString("adress");
                LocalDate dateCreated = rs.getDate("dateCreated").toLocalDate();
                User user = new User(password,isAdmin,authToken,lastLogin,adress,dateCreated);
                user.setId(id);
                return user;
            }
        }
        catch (SQLException e){
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public void save(User entity) {
        Connection con = dbUtils.getConnection();
        LocalDateTime lastLogin = entity.getLastLogin();
        LocalDate dateCreated = entity.getDateCreated();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO USER(email,password,isAdmin,authToken,lastLogin,adress,dateCreated) values (?,?,?,?,?,?,?)")){
            ps.setString(1,entity.getId());
            ps.setString(2,entity.getPassword());
            ps.setBoolean(3,entity.isAdmin());
            ps.setString(4,entity.getAuthToken());
            ps.setObject(5,lastLogin);
            ps.setString(6,entity.getAdress());
            ps.setObject(7,dateCreated);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        Connection con = dbUtils.getConnection();
        LocalDateTime lastLogin = entity.getLastLogin();
        LocalDate dateCreated = entity.getDateCreated();
        try (PreparedStatement ps = con.prepareStatement("UPDATE Users SET password=?, isAdmin=?, authToken=?, lastLogin=?, adress=?, dateCreated=? WHERE id=?")){
            ps.setString(1,entity.getPassword());
            ps.setBoolean(2,entity.isAdmin());
            ps.setString(3,entity.getAuthToken());
            ps.setObject(4,lastLogin);
            ps.setString(5,entity.getAdress());
            ps.setObject(6,dateCreated);
            ps.setString(7, entity.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(String s) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE * FROM Users WHERE id=?")) {
            ps.setString(1,s);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
