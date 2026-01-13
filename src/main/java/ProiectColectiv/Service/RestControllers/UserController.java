package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.LoginRequest;
import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.Collections;
import java.time.LocalDateTime;


@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserRepo userRepo;


   /* @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User repoUser = userRepo.findById(user.getId());
        if (BCrypt.verifyer().verify(user.getPassword().toCharArray(), repoUser.getPassword()).verified){
            return new ResponseEntity<>(repoUser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User repoUser = userRepo.findById(loginRequest.getEmail());

        if (repoUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(), repoUser.getPassword()).verified) {

            String token = UUID.randomUUID().toString();

            repoUser.setAuthToken(token);
            repoUser.setLastLogin(LocalDateTime.now());

            userRepo.update(repoUser);

            return new ResponseEntity<>(Collections.singletonMap("authToken", token), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        userRepo.save(user);
        return user;
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody User user) {
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        userRepo.updatePassword(user);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepo.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userRepo.delete(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @GetMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if(userRepo.findById(email) != null) {
            return new ResponseEntity<>(userRepo.findById(email), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String email) {
        User foundUser = userRepo.findById(email);
        if (foundUser != null) {
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
