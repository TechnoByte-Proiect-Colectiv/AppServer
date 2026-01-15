package ProiectColectiv.Repository.DatabaseRepo;

import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Order;
import ProiectColectiv.Domain.Product;
import ProiectColectiv.Domain.Review;
import ProiectColectiv.Repository.Interfaces.IReviewRepo;
import ProiectColectiv.Repository.Utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class ReviewRepo implements IReviewRepo {
    private final JdbcUtils dbUtils;

    public ReviewRepo(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Review findById(CompositeKey<Integer, String> key) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Reviews WHERE idUser = ? AND idProduct = ?";
        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, key.key2());
            preStmt.setInt(2, key.key1());
            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) return extractReviewFromResultSet(rs);
            }
        } catch (SQLException e) { System.err.println(e); }
        return null;
    }

    @Override
    public void save(Review entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Reviews(idProduct,idUser,rating,title,description,createdAt,verifiedPurchase) VALUES (?,?,?,?,?,?,?)")){
            ps.setInt(1, entity.getId().key1());
            ps.setString(2, entity.getId().key2());
            ps.setInt(3, entity.getRating());
            ps.setString(4, entity.getTitle());
            ps.setString(5, entity.getDescription());

            LocalDate date = (entity.getCreated_at() != null) ? entity.getCreated_at() : LocalDate.now();
            ps.setDate(6, Date.valueOf(date));

            ps.setBoolean(7, entity.getVerifiedPurchase() != null ? entity.getVerifiedPurchase() : false);
            ps.executeUpdate();
        } catch (SQLException e){
            System.err.println("SQL Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Review entity) {
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE Reviews SET rating = ?, title = ?, description = ?, verifiedPurchase = ? WHERE idProduct = ? AND idUser = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, entity.getRating());
            ps.setString(2, entity.getTitle());
            ps.setString(3, entity.getDescription());
            ps.setBoolean(4, entity.getVerifiedPurchase());
            ps.setInt(5, entity.getId().key1()); // productId
            ps.setString(6, entity.getId().key2()); // userId
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
        }
    }

    @Override
    public void delete(CompositeKey<Integer, String> key) {
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM Reviews WHERE idProduct = ? AND idUser = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, key.key1());
            ps.setString(2, key.key2());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        }
    }

    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        String userID = rs.getString("idUser");
        Date sqlDate = rs.getDate("createdAt");
        LocalDate orderDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        Integer productId = rs.getInt("idProduct");
        Integer rating = rs.getInt("rating");
        String title = rs.getString("title");
        String description = rs.getString("description");
        Boolean verifiedPurchase = rs.getBoolean("verifiedPurchase");

        return new Review(userID, productId, rating, title, description, orderDate, verifiedPurchase);
    }

    @Override
    public Iterable<Review> findAllForProduct(Integer productId) {
        ArrayList<Review> reviews = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Reviews WHERE idProduct = ?")) {
            preStmt.setInt(1, productId);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    Review review = extractReviewFromResultSet(rs);
                    reviews.add(review);
                }
                return reviews;
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById: " + e);
        }
        return null;
    }

    @Override
    public Iterable<Review> findAllForUser(String userId) {
        ArrayList<Review> reviews = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Reviews WHERE idUser = ?")) {
            preStmt.setString(1, userId);
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    Review review = extractReviewFromResultSet(rs);
                    reviews.add(review);
                }
                return reviews;
            }
        } catch (SQLException e) {
            System.err.println("Error DB findAllForUser: " + e);
        }
        return null;
    }
}
