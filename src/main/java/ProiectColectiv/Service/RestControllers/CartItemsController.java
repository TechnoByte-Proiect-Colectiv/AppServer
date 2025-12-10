package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.CartItem;
import ProiectColectiv.Repository.Interfaces.ICartItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cartItem")
public class CartItemsController {
    @Autowired
    private ICartItemRepo cartItemRepo;

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getItemsInOrder(@PathVariable String orderId) {
        return new ResponseEntity<>(cartItemRepo.findAllForOrder(orderId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addItemToCart(@RequestBody CartItem cartItem) {
        cartItemRepo.save(cartItem);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateItem(@RequestBody CartItem cartItem) {
        cartItemRepo.update(cartItem);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteItem(@RequestBody CartItem cartItem) {
        cartItemRepo.delete(cartItem.getId());
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
}
