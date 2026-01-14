package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.LoginRequest;
import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import ProiectColectiv.Repository.Utils.JWTUtils;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserRepo userRepo;
    private final JWTUtils jwtUtils;

    @Autowired
    public UserController(IUserRepo userRepo, JWTUtils jwtUtils) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User repoUser = userRepo.findById(loginRequest.getEmail());

        if (repoUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(), repoUser.getPassword()).verified) {

            String token = jwtUtils.generateToken(repoUser.getEmail(), repoUser.isAdmin());

            repoUser.setLastLogin(LocalDateTime.now());
            userRepo.update(repoUser);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", repoUser.getEmail());
            response.put("isAdmin", repoUser.isAdmin());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (userRepo.findById(user.getEmail()) != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        user.setDateCreated(LocalDate.now());
        user.setLastLogin(LocalDateTime.now());

        String token = jwtUtils.generateToken(user.getEmail(), user.isAdmin());
        userRepo.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User existingUser = userRepo.findById(id);
        if (existingUser == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAdress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(BCrypt.withDefaults().hashToString(12, updatedUser.getPassword().toCharArray()));
        }

        userRepo.update(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userRepo.delete(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @GetMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userRepo.findById(email);
        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>("Missing Token", HttpStatus.UNAUTHORIZED);
        }

        String jwt = token.substring(7);

        if (jwtUtils.validateToken(jwt)) {
            String email = jwtUtils.extractEmail(jwt);
            User foundUser = userRepo.findById(email);
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }
    }
}