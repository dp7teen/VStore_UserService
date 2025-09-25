package com.dp.vstore_userservice.dtos;

import com.dp.vstore_userservice.annotations.ValidateRole;
import com.dp.vstore_userservice.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleUpdateDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "At least one Role is required")
    @ValidateRole(enumClass = Role.class, message = "Role must be one of ADMIN, CUSTOMER")
    private List<String> roles;
}
