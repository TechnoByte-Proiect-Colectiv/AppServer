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

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User repoUser = userRepo.findById(loginRequest.getEmail());

        if (repoUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(), repoUser.getPassword()).verified) {
            String token = jwtUtils.generateToken(repoUser.getEmail(), repoUser.isAdmin());

            repoUser.setAuthToken(token);
            repoUser.setLastLogin(LocalDateTime.now());
            userRepo.update(repoUser);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", repoUser.getEmail());
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

        // Hash parola
        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));

        // Date implicite
        user.setDateCreated(LocalDate.now());
        user.setLastLogin(LocalDateTime.now());

        // Generare token imediat
        String token = jwtUtils.generateToken(user.getEmail(), user.isAdmin());
        user.setAuthToken(token);

        userRepo.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Missing or invalid token", HttpStatus.UNAUTHORIZED);
        }

        String jwt = authHeader.substring(7);
        if (jwtUtils.validateToken(jwt)) {
            String email = jwtUtils.extractEmail(jwt);
            User user = userRepo.findById(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedData) {
        User existingUser = userRepo.findById(id);
        if (existingUser == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        existingUser.setFirstName(updatedData.getFirstName());
        existingUser.setLastName(updatedData.getLastName());
        existingUser.setAdress(updatedData.getAddress());
        existingUser.setPhoneNumber(updatedData.getPhoneNumber());

        userRepo.update(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
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
}