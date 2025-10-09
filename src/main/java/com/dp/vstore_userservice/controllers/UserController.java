package com.dp.vstore_userservice.controllers;

import com.dp.vstore_userservice.dtos.*;
import com.dp.vstore_userservice.exceptions.UserAlreadyPresentException;
import com.dp.vstore_userservice.exceptions.UserNotFoundException;
import com.dp.vstore_userservice.models.User;
import com.dp.vstore_userservice.services.UserService;
import com.dp.vstore_userservice.utility.GetPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@EnableMethodSecurity
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignupRequestDto dto) throws UserNotFoundException,
            UserAlreadyPresentException {
        User user = userService.signup(dto.getUsername(), dto.getEmail(), dto.getPassword(),
                dto.getRole());
        return new ResponseEntity<>(
                UserDto.from(user), HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequestDto dto) throws UserNotFoundException {
        return new ResponseEntity<>(
                TokenDto.from(userService.login(dto.getEmail(), dto.getPassword())),
                HttpStatus.OK
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() throws UserNotFoundException {
        UserDetails details = GetPrincipal.principal();
        return new ResponseEntity<>(
                userService.me(details.getUsername()),
                HttpStatus.OK
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<UserDto> updateProfile(@Valid  @RequestBody UpdateProfileDto dto) throws UserNotFoundException {
        return new ResponseEntity<>(
                userService.updateProfile(dto, GetPrincipal.principal().getUsername()),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/update/role")
    public ResponseEntity<UserDto> updateRole(@Valid @RequestBody RoleUpdateDto dto) throws UserNotFoundException {
        return new ResponseEntity<>(
                userService.updateRole(dto), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody DeleteUserDto dto) throws UserNotFoundException {
        return new ResponseEntity<>(
                userService.deleteUser(dto.getEmail()), HttpStatus.OK
        );
    }
}
