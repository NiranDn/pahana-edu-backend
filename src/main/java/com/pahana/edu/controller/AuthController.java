package com.pahana.edu.controller;

import com.pahana.edu.dto.ApiResponse;
import com.pahana.edu.dto.LoginRequest;
import com.pahana.edu.dto.RegisterRequest;
import com.pahana.edu.model.User;
import com.pahana.edu.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = authenticationService.register(registerRequest.getUsername(), registerRequest.getPassword());
            Map<String, Object> userData = new HashMap<>();
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("username", user.getUsername());
            userDetails.put("password", registerRequest.getPassword());
            userData.put("user", userDetails);
            
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", userData));
        } catch (RuntimeException e) {
            Map<String, Object> errorData = new HashMap<>();
            Map<String, String> errors = new HashMap<>();
            errors.put("username", e.getMessage());
            errorData.put("user", errors);
            
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Registration failed", errorData));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("user", errors);
        
        return ResponseEntity.badRequest().body(new ApiResponse(false, "Validation failed", errorData));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (isAuthenticated) {
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
}
