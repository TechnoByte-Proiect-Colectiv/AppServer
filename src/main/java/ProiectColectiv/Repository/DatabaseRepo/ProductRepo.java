package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.Filter;
import ProiectColectiv.Repository.Utils.JdbcUtils;
import javax0.levenshtein.Levenshtein;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductRepo implements IProductRepo {
    private final JdbcUtils dbUtils;

    public ProductRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
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

    @Override
    public Iterable<Product> getMostSoldProducts(int nr) {
        List<Product> products = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Products ORDER BY nrSold DESC LIMIT ?")){
            ps.setInt(1,nr);
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
    public Iterable<Product> filteredSearch(Filter filter,String searchInput) {

        StringBuilder condition = new StringBuilder();
        List<Integer> params = new ArrayList<>();
        if(filter!=null) {
            condition = new StringBuilder(" WHERE 1=1");
            if(filter.isFilterInStock())
                condition.append(" AND nrItems > 0");
            if(filter.isFilterPriceLower()) {
                condition.append(" AND price < ?");
                params.add(filter.getPriceLower());
            }
            if(filter.isFilterPriceHigher()) {
                condition.append(" AND price > ?");
                params.add(filter.getPriceHigher());
            }
        }

        List<Product> products = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Products" + condition)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setInt(i+1, params.get(i));
            }
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
                    int matches = 0;
                    searchInput = searchInput == null ? "" : searchInput.strip().replaceAll("\\s+"," ").toLowerCase();
                    if (searchInput.isEmpty()) {
                        products.add(product);
                        continue;
                    }
                    String[] inputWords = searchInput.split(" ");
                    String[] nameWords = product.getName().strip().replaceAll("\\s+"," ").toLowerCase().split(" ");
                    for (String inputWord : inputWords) {
                        for (String nameWord : nameWords) {
                            if (Levenshtein.distance(inputWord, nameWord) < 3) {
                                matches++;
                                break;
                            }
                        }
                    }
                    if ((double) matches / inputWords.length >= 0.7)
                        products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return products;
    }
}
