package ProiectColectiv.Service;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.DatabaseRepo.UserRepo;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Utils.Filter;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IProductRepo productRepo;

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Product p = productRepo.findById(id);
        if(p == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/product")
    public Product create(@RequestBody Product product) {
        productRepo.save(product);
        return product;
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        productRepo.delete(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/product/most-sold")
    public Iterable<Product> getMostSold(@RequestParam int nr) {
        return productRepo.getMostSoldProducts(nr);
    }

    @GetMapping("/product/search")
    public Iterable<Product> filteredSearch(
            @RequestParam(required = false) String search,
            @RequestBody(required = false) Filter filter) {

        return productRepo.filteredSearch(filter, search);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User repoUser = userRepo.findById(user.getId());
        if (BCrypt.verifyer().verify(user.getPassword().toCharArray(), repoUser.getPassword()).verified){
            return new ResponseEntity<>(repoUser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/user/create")
    public User create(@RequestBody User user) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        userRepo.save(user);
        return user;
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody User user) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        userRepo.update(user);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }
}
