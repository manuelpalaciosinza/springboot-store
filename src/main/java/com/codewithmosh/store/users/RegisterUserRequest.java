package com.codewithmosh.store.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 8, max = 255, message = "Name must be between 8 and 255 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be in lowercase")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 8 and 255 characters long")
    private String password;
}
