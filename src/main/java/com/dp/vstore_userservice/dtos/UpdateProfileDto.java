package com.dp.vstore_userservice.dtos;

import com.dp.vstore_userservice.annotations.ValidateRole;
import com.dp.vstore_userservice.models.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateProfileDto {
    @Size(min = 3, max = 20, message = "Username must be between 3–20 characters")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 chars, contain 1 uppercase, 1 lowercase, 1 digit, 1 special char"
    )
    private String password;

}
