package com.example.BackendAssignment.Controller;

import com.example.BackendAssignment.Entity.User;
import com.example.BackendAssignment.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public User signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        // Validate username and password
        if (!isValidUsername(user.getUsername()) || !isValidPassword(user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password format");
        }

        // Check if the username is already taken
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Set the encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userService.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with ID: " + savedUser.getId());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestBody User updatedUser
    ) {
        // Implement authentication and authorization logic
        // to ensure the logged-in user is updating their own profile

        // Check if the user exists
        User existingUser = userService.findById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username not found");
        }

        // Validate and update fields as needed
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setMobile(updatedUser.getMobile());

        // If the password is provided, update it
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            if (!isValidPassword(updatedUser.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password format");
            }
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userService.updateUser(existingUser);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Validate username format
    private boolean isValidUsername(String username) {
        // Implement your username validation logic
        // For example, check if it contains only characters and digits with a length of 4 to 15
        // You can customize this based on your requirements
        return username.matches("^[a-zA-Z0-9]{4,15}$");
    }

    // Validate password format
    private boolean isValidPassword(String password) {
        // Implement your password validation logic
        // For example, check if it is 8 to 15 characters with at least 1 upper, 1 lower, 1 digit, and 1 special character
        // You can customize this based on your requirements
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$");
    }
}
