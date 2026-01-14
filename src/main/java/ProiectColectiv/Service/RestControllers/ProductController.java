package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final String IMAGE_FOLDER = "Images/";

    @Autowired
    private IProductRepo productRepo;

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer id) {
        try {
            String filePath = IMAGE_FOLDER + id + ".png";
            File file = new File(filePath);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable String slug) {
        Product p = productRepo.findBySlug(slug);
        if(p == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        productRepo.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productRepo.delete(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/most-sold")
    public ResponseEntity<?> getMostSoldProducts(@RequestParam int nr) {
        return new ResponseEntity<>(productRepo.getMostSoldProducts(nr), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> filteredSearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Float priceLower,
            @RequestParam(required = false) Float priceHigher,
            @RequestParam(required = false) Boolean inStock)
    {
        Filter filter = new Filter(priceLower,priceHigher,inStock);
        return new ResponseEntity<>(productRepo.filteredSearch(filter, search), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(productRepo.getAllProducts(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        productRepo.update(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
