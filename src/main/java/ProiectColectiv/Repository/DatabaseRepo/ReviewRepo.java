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
    public Review findById(CompositeKey<Integer, String> integerStringCompositeKey) {
        Connection con = dbUtils.getConnection();
        String query = "SELECT * FROM Orders WHERE idUser = ? and idProduct = ?";

        try (PreparedStatement preStmt = con.prepareStatement(query)) {
            preStmt.setString(1, integerStringCompositeKey.key2());
            preStmt.setInt(2, integerStringCompositeKey.key1());

            try (ResultSet rs = preStmt.executeQuery()) {
                if (rs.next()) {
                    return extractReviewFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB findById Order: " + e);
        }
        return null;
    }

    @Override
    public void save(Review entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Reviews(idProduct,idUser,rating,title,description,createdAt,verifiedPurchase) VALUES " +
                "(?,?,?,?,?,?,?)")){
            ps.setInt(1,entity.getId().key1());
            ps.setString(2,entity.getId().key2());
            ps.setInt(3,entity.getRating());
            ps.setString(4,entity.getTitle());
            ps.setString(5,entity.getDescription());
            ps.setObject(6, Date.valueOf(entity.getCreated_at()));
            ps.setBoolean(7,entity.getVerifiedPurchase());
            ps.executeUpdate();

        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Review entity) {

    }

    @Override
    public void delete(CompositeKey<Integer, String> integerStringCompositeKey) {

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
