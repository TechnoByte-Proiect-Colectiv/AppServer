package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.CompositeKey;
import ProiectColectiv.Domain.Product;
import ProiectColectiv.Domain.Review;
import ProiectColectiv.Repository.Interfaces.ICartItemRepo;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
import ProiectColectiv.Repository.Interfaces.IReviewRepo;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private IReviewRepo reviewRepo;
    @Autowired
    private IUserRepo userRepo;
    @Autowired private ProiectColectiv.Repository.Utils.JWTUtils jwtUtils;

    @PostMapping
    public ResponseEntity<?> createOrUpdateReview(@RequestHeader("Authorization") String token, @RequestBody Review review) {
        try {
            if (token == null || !token.startsWith("Bearer ") || token.length() < 15) {
                return new ResponseEntity<>("Token missing or too short", HttpStatus.UNAUTHORIZED);
            }
            String jwt = token.substring(7).trim();

            if (jwt.split("\\.").length != 3) {
                return new ResponseEntity<>("Invalid JWT format", HttpStatus.UNAUTHORIZED);
            }

            String email = jwtUtils.extractEmail(jwt);

            if (review.getCreated_at() == null) {
                review.setCreated_at(LocalDate.now());
            }

            CompositeKey<Integer, String> id = new CompositeKey<>(review.getProductId(), email);
            Review existing = reviewRepo.findById(id);

            if (existing != null) {
                reviewRepo.update(review);
                return new ResponseEntity<>(review, HttpStatus.OK);
            }

            reviewRepo.save(review);
            return new ResponseEntity<>(review, HttpStatus.CREATED);

        } catch (io.jsonwebtoken.MalformedJwtException e) {
            return new ResponseEntity<>("Token is malformed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteReview(@RequestHeader("Authorization") String token, @PathVariable Integer productId) {
        String email = jwtUtils.extractEmail(token.replace("Bearer ", ""));
        reviewRepo.delete(new CompositeKey<>(productId, email));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getAllReviews(@PathVariable Integer productId) {
        return new ResponseEntity<>(reviewRepo.findAllForProduct(productId),HttpStatus.OK);
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<?> getAllReviewsForUser(@RequestHeader("Authorization") String token) {
        String email = jwtUtils.extractEmail(token.replace("Bearer ", ""));
        return new ResponseEntity<>(reviewRepo.findAllForUser(email), HttpStatus.OK);
    }
}
