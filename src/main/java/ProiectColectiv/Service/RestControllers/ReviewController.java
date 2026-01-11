package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Domain.Review;
import ProiectColectiv.Repository.Interfaces.ICartItemRepo;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
import ProiectColectiv.Repository.Interfaces.IReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private IReviewRepo reviewRepo;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        reviewRepo.save(review);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getAllReviews(@PathVariable Integer productId) {
        return new ResponseEntity<>(reviewRepo.findAllForProduct(productId),HttpStatus.OK);
    }
}
