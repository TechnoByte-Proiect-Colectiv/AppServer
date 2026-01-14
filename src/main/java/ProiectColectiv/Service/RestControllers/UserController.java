package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.Address;
import ProiectColectiv.Domain.LoginRequest;
import ProiectColectiv.Domain.User;
import ProiectColectiv.Repository.DatabaseRepo.UserRepo;
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

            String token = jwtUtils.generateToken(repoUser.getEmail(), false);

            repoUser.setAuthToken(token);
            repoUser.setLastLogin(LocalDateTime.now());
            userRepo.update(repoUser);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("user", repoUser.getEmail());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/address")
    public ResponseEntity<?> getUserAddresses(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");

            String userEmail = jwtUtils.extractEmail(token);
            User user = userRepo.findById(userEmail);

            if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            return ResponseEntity.ok(user.getAddresses());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/address")
    public ResponseEntity<?> addAddress(@RequestHeader("Authorization") String authHeader, @RequestBody Address address) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");

            String userEmail = jwtUtils.extractEmail(token);

            if (userRepo instanceof UserRepo) {
                ((UserRepo) userRepo).addAddress(userEmail, address);
            } else {
                // Fallback
            }

            return ResponseEntity.ok(address);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add address: " + e.getMessage());
        }
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<?> updateAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String addressId,
            @RequestBody Address address) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");

            address.setId(addressId);

            if (userRepo instanceof UserRepo) {
                ((UserRepo) userRepo).updateAddress(address);
            }

            return ResponseEntity.ok(address);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update address: " + e.getMessage());
        }
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@RequestHeader("Authorization") String authHeader, @PathVariable String addressId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");

            if (userRepo instanceof UserRepo) {
                ((UserRepo) userRepo).deleteAddress(addressId);
            }

            return ResponseEntity.ok("Address deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete address: " + e.getMessage());
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (userRepo.findById(user.getEmail()) != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        user.setDateCreated(LocalDate.now());
        userRepo.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
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

        if(updatedUser.getPassword() != null) existingUser.setPassword(BCrypt.withDefaults().hashToString(12, existingUser.getPassword().toCharArray()));;

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