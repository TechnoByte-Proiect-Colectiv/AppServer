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
import java.util.Collections;
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

        // Verificam parola
        if (BCrypt.verifyer().verify(loginRequest.getPassword().toCharArray(), repoUser.getPassword()).verified) {

            // PASUL 1: Generam Token-ul JWT folosind Utils
            // Aici presupunem ca userul nu e admin (false).
            // Daca ai un camp isAdmin in User, foloseste: repoUser.isAdmin()
            String token = jwtUtils.generateToken(repoUser.getEmail(), false);

            // PASUL 2: Actualizam doar data logarii
            // NOTA: La JWT nu e obligatoriu sa salvezi token-ul in baza de date (stateless).
            // Daca vrei totusi sa il salvezi pentru o verificare extra, poti lasa linia cu setAuthToken.
            // repoUser.setAuthToken(token); <--- O poti scoate daca vrei full stateless

            repoUser.setLastLogin(LocalDateTime.now());
            userRepo.update(repoUser);

            // PASUL 3: Returnam token-ul intr-un format JSON frumos
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("email", repoUser.getEmail());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user) {
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