package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductRepo implements IProductRepo {
    private final JdbcUtils dbUtils;

    public ProductRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Iterable<Product> findAll() {
        List<Product> products = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Products")){
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getFloat("price"),
                            rs.getInt("nrItems"),
                            rs.getInt("nrSold")
                    );
                    product.setId(rs.getInt("id"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return products;
    }

    @Override
    public Product findById(Integer integer) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Products where id=?")){
            preStmt.setInt(1,integer);
            try (ResultSet rs=preStmt.executeQuery()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Float price = rs.getFloat("price");
                Integer nrItems = rs.getInt("nrItems");
                Integer nrSold = rs.getInt("nrSold");
                Product product = new Product(name,description,price,nrItems,nrSold);
                product.setId(id);
                return product;
            }
        }
        catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return null;
    }

    @Override
    public void update(Product entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Products SET name=?,description=?, price=?, nrItems=?, nrSold=? WHERE id=?")){
            ps.setString(1,entity.getName());
            ps.setString(2,entity.getDescription());
            ps.setFloat(3,entity.getPrice());
            ps.setInt(4,entity.getQuantity());
            ps.setInt(5,entity.getNrSold());
            ps.setInt(6,entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE * FROM Products WHERE id=?")) {
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void save(Product entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Products(name,description,price,nrItems,nrSold) values (?,?,?,?,?)")){
            ps.setString(1,entity.getName());
            ps.setString(2,entity.getDescription());
            ps.setFloat(3,entity.getPrice());
            ps.setInt(4,entity.getQuantity());
            ps.setInt(5,entity.getNrSold());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
