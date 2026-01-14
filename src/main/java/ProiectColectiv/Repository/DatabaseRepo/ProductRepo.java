package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.Filter;
import ProiectColectiv.Repository.Utils.JdbcUtils;
import javax0.levenshtein.Levenshtein;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductRepo implements IProductRepo {
    private final JdbcUtils dbUtils;
    private final String IMAGE_FOLDER = "Images/"; // Constanta pentru folder

    public ProductRepo(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Product findById(Integer id) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Products WHERE id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next())
                    return mapProductFromDB(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById: " + e);
        }
        return null;
    }

    @Override
    public Product findBySlug(String slug){
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Products WHERE slug=?")) {
            preStmt.setString(1, slug);
            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next())
                    return mapProductFromDB(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error DB findBySlug: " + e);
        }
        return null;
    }

    @Override
    public void update(Product entity) {
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE Products SET name=?, description=?, slug=?, brand=?, price=?," +
                " nrItems=?, currency=?, nrSold=?, category=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, entity.getSlug());
            ps.setString(4, entity.getBrand());
            ps.setFloat(5, entity.getPrice());
            ps.setInt(6, entity.getQuantity());
            ps.setString(7, entity.getCurrency());
            ps.setInt(8, entity.getNrSold());
            ps.setString(9, entity.getCategory());
            ps.setInt(10, entity.getId());

            ps.executeUpdate();

            if (entity.getFileData() != null) {
                try (FileOutputStream fos = new FileOutputStream(IMAGE_FOLDER + entity.getId() + ".png")) {
                    fos.write(entity.getFileData());
                } catch (IOException e) {
                    System.err.println("Error updating image file: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error DB update: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM Products WHERE id=?")) {
            ps.setInt(1, id);
            File file = new File(IMAGE_FOLDER + id + ".png");
            file.delete();
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB delete: " + e.getMessage());
        }
    }

    @Override
    public void save(Product entity) {
        Connection con = dbUtils.getConnection();

        String query = """
        INSERT INTO Products(name, description, slug, brand, price, nrItems,
                             currency, nrSold, category)
        VALUES (?,?,?,?,?,?,?,?,?)
        """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, entity.getSlug());
            ps.setString(4, entity.getBrand());
            ps.setFloat(5, entity.getPrice());
            ps.setInt(6, entity.getQuantity());
            ps.setString(7, entity.getCurrency());
            ps.setInt(8, entity.getNrSold());
            ps.setString(9, entity.getCategory());

            ps.executeUpdate();

            // 🔥 IA ID-ul generat
            try (PreparedStatement ps2 = con.prepareStatement("SELECT last_insert_rowid()");
                 ResultSet rs = ps2.executeQuery()) {

                if (rs.next()) {
                    int id = rs.getInt(1);

                    // 🔥 ASTA LIPSEA
                    entity.setId(id);

                    // Salvează imaginea
                    if (entity.getFileData() != null) {
                        try (FileOutputStream fos =
                                     new FileOutputStream(IMAGE_FOLDER + id + ".png")) {
                            fos.write(entity.getFileData());
                        } catch (IOException e) {
                            System.err.println("Error saving image file: " + e.getMessage());
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error DB save: " + e.getMessage());
        }
    }


    @Override
    public Iterable<Product> getMostSoldProducts(int nr) {
        List<Product> products = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Products ORDER BY nrSold DESC LIMIT ?")) {
            ps.setInt(1, nr);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProductFromDB(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB getMostSold: " + e.getMessage());
        }
        return products;
    }

    @Override
    public Iterable<Product> filteredSearch(Filter filter, String searchInput) {
        StringBuilder condition = new StringBuilder();
        List<Float> params = new ArrayList<>();

        if (filter != null) {
            condition = new StringBuilder(" WHERE 1=1");
            if (filter.isFilterInStock() != null && filter.isFilterInStock())
                condition.append(" AND nrItems > 0");
            if (filter.getPriceLower() != null) {
                condition.append(" AND price < ?");
                params.add(filter.getPriceLower());
            }
            if (filter.getPriceHigher() != null) {
                condition.append(" AND price > ?");
                params.add(filter.getPriceHigher());
            }
        }

        List<Product> products = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Products" + condition)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setFloat(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = mapProductFromDB(rs);

                    int matches = 0;
                    String processedInput = (searchInput == null) ? "" : searchInput.strip().replaceAll("\\s+", " ").toLowerCase();

                    if (processedInput.isEmpty()) {
                        products.add(product);
                        continue;
                    }

                    String[] inputWords = processedInput.split(" ");
                    String[] nameWords = product.getName().strip().replaceAll("\\s+",
                            " ").toLowerCase().split(" ");

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
            System.err.println("Error DB filteredSearch: " + e.getMessage());
        }
        return products;
    }

    @Override
    public Iterable<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Products")) {
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProductFromDB(rs));
                }
                return products;
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById: " + e);
        }
        return null;
    }

    private Product mapProductFromDB(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String slug = rs.getString("slug");
        String brand = rs.getString("brand");
        Float price = rs.getFloat("price");
        Integer quantity = rs.getInt("nrItems");
        String currency = rs.getString("currency");

        Integer nrSold = rs.getInt("nrSold");
        String category = rs.getString("category");

        byte[] fileData = null;
            try (FileInputStream fis = new FileInputStream(IMAGE_FOLDER + id + ".png")) {
                fileData = fis.readAllBytes();
            } catch (IOException e) {
                System.err.println("Warning: Image not found for product " + name);
            }

        Product product = new Product(name, description, slug, brand, price, quantity, currency, nrSold, category, fileData);
        product.setId(id);

        return product;
    }
}