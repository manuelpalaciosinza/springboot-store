package com.codewithmosh.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must follow standard format")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
