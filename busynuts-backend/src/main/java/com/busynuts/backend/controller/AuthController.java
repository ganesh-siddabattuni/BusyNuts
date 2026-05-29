package com.busynuts.backend.controller;

import com.busynuts.backend.model.User;
import com.busynuts.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Base URL pathway for authentication
@CrossOrigin(origins = "*")  // Allows our future Angular app on another port to talk to this API
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully with ID: " + registeredUser.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
    try {
        // Authenticate via our service layer
        User user = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        
        // Clear the password out of the response object so it stays hidden
        user.setPassword(null); 
        
        // Return the authenticated user profile back to the client
        return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
}