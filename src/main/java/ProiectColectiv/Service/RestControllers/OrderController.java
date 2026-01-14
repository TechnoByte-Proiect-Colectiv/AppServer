package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.Order;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private IOrderRepo orderRepo;
    @Autowired
    private IUserRepo userRepo;

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId) {
        return new ResponseEntity<>(orderRepo.findById(orderId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        orderRepo.save(order);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/user/{authToken}")
    public ResponseEntity<?> getOrdersByAuthToken(@PathVariable String authToken) {
        String userId = userRepo.findByToken(authToken).getId();
        return new ResponseEntity<>(orderRepo.getAllOrdersByUser(userId), HttpStatus.OK);
    }
}
