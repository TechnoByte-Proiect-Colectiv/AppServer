package ProiectColectiv.Service;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.DatabaseRepo.ProductRepo;
import ProiectColectiv.Repository.DatabaseRepo.UserRepo;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PrinterGraphics;

@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IProductRepo productRepo;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Product p = productRepo.findById(id);
        if(p == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        productRepo.save(product);
        return product;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        productRepo.delete(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/most-sold")
    public Iterable<Product> getMostSold(@RequestParam int nr) {
        return productRepo.getMostSoldProducts(nr);
    }

    @PostMapping("/search")
    public Iterable<Product> filteredSearch(
            @RequestParam(required = false) String search,
            @RequestBody(required = false) Filter filter) {

        return productRepo.filteredSearch(filter, search);
    }
}
