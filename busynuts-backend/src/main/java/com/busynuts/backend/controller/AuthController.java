package com.busynuts.backend.controller;

import com.busynuts.backend.dto.AuthRequest;
import com.busynuts.backend.dto.AuthResponse;
import com.busynuts.backend.model.User;
import com.busynuts.backend.security.JwtUtil;
import com.busynuts.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService; // Your custom DB service

    @Autowired
    private JwtUtil jwtUtil;

    // --- SIGNUP (Untouched) ---
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully with ID: " + registeredUser.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- LOGIN (Now uses MySQL + JWT!) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // 1. Verify against your ACTUAL MySQL database using your existing service!
            User user = userService.loginUser(request.getUsername(), request.getPassword());

            // 2. Grab their role from the database (fallback to consumer if null)
            String role = user.getRole() != null ? user.getRole().name() : "ROLE_CONSUMER";

            // 3. Generate the JWT keycard
            String token = jwtUtil.generateToken(user.getUsername(), role);

            // 4. Send the token back to Angular!
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), role));

        } catch (Exception e) {
            e.printStackTrace(); // This will print the exact reason to your Java console
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}