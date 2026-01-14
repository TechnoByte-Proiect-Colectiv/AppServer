package ProiectColectiv.Repository.Interfaces;

import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Review;

public interface IReviewRepo extends Repository<CompositeKey<Integer,String>,Review> {
    Iterable<Review> findAllForProduct(Integer productId);
    Iterable<Review> findAllForUser(String userId);
}
