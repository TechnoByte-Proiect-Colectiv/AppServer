package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.Order;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        return new ResponseEntity<>(orderRepo.findById(orderId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        orderRepo.save(order);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
}
