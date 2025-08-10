package com.pahana.edu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {
    @Email(message = "Username must be a valid email address")
    private String username;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "Password must be at least 8 characters long and contain both letters and numbers"
    )
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
