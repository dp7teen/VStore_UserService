package com.dp.vstore_userservice.services;

import com.dp.vstore_userservice.dtos.RoleUpdateDto;
import com.dp.vstore_userservice.dtos.SignupRequestDto;
import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.exceptions.UserAlreadyPresentException;
import com.dp.vstore_userservice.exceptions.UserNotFoundException;
import com.dp.vstore_userservice.models.Role;
import com.dp.vstore_userservice.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User signup(String userName, String email, String password, List<String> role) throws UserNotFoundException,
            UserAlreadyPresentException;

    String login(String email, String password);

    User me() throws UserNotFoundException;

    User updateProfile(UpdateProfileDto dto) throws UserNotFoundException;

    String updateRole(RoleUpdateDto dto) throws UserNotFoundException;

    String deleteUser(String email) throws UserNotFoundException;
}
