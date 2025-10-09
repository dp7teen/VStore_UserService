package com.dp.vstore_userservice.services;

import com.dp.vstore_userservice.dtos.RoleUpdateDto;
import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.dtos.UserDto;
import com.dp.vstore_userservice.exceptions.UserAlreadyPresentException;
import com.dp.vstore_userservice.exceptions.UserNotFoundException;
import com.dp.vstore_userservice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User signup(String userName, String email, String password, List<String> role) throws UserNotFoundException,
            UserAlreadyPresentException;

    String login(String email, String password) throws UserNotFoundException;

    UserDto me(String email) throws UserNotFoundException;

    UserDto updateProfile(UpdateProfileDto dto, String email) throws UserNotFoundException;

    UserDto updateRole(RoleUpdateDto dto) throws UserNotFoundException;

    String deleteUser(String email) throws UserNotFoundException;
}
