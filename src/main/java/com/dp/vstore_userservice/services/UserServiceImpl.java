package com.dp.vstore_userservice.services;

import com.dp.vstore_userservice.dtos.RoleUpdateDto;
import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.exceptions.UserAlreadyPresentException;
import com.dp.vstore_userservice.exceptions.UserNotFoundException;
import com.dp.vstore_userservice.models.Role;
import com.dp.vstore_userservice.models.User;
import com.dp.vstore_userservice.repositories.UserRepository;
import com.dp.vstore_userservice.security.services.JWTHelper;
import com.dp.vstore_userservice.services.profileUpdater.*;
import com.dp.vstore_userservice.utility.GetPrincipal;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTHelper jwthelper;
    private final List<ProfileUpdater> profileUpdaters;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JWTHelper jwthelper,
                           List<ProfileUpdater> profileUpdaters) {
        this.profileUpdaters = profileUpdaters;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwthelper = jwthelper;
    }

    private Optional<User> findUser(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email);
    }

    @Override
    public User signup(String userName, String email, String password, List<String> roles) throws UserNotFoundException,
            UserAlreadyPresentException {
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyPresentException(String.format("User already present with this email : '%s'", email));
        }
        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(new ArrayList<>());
        roles.forEach(role -> user.getRole().add(Role.valueOf(role.toUpperCase())));
        userRepository.save(user);
        return user;
    }

    @Override
    public String login(String email, String password) throws UserNotFoundException {
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with email : '%s' not found", email));
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", user.getId());
        claims.put("roles", user.getRole().stream().map(Enum::name).toList());
        claims.put("userName", user.getUserName());

        return jwthelper.generateToken(user, claims);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public User me(String email) throws UserNotFoundException {
        UserDetails userDetails = GetPrincipal.principal();
        return findUser(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException(String.format("User with email : '%s' not found", userDetails.getUsername())));
    }

    @Override
    @CachePut(value = "users", key = "#email")
    public User updateProfile(UpdateProfileDto dto, String email) throws UserNotFoundException {
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with email : '%s' not found", email));
        }
        User user = optionalUser.get();

        for (ProfileUpdater profileUpdater : profileUpdaters) {
            profileUpdater.update(user, dto);
        }
        return userRepository.save(user);
    }

    @Override
    @CachePut(value = "users", key = "#dto.getEmail()")
    public String updateRole(RoleUpdateDto dto) throws UserNotFoundException {
        Optional<User> optionalUser = findUser(dto.getEmail());
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with email : '%s' not found", dto.getEmail()));
        }
        User user = optionalUser.get();
        RoleUpdater profileUpdater = new RoleUpdater();
        profileUpdater.update(user, dto);

        userRepository.save(user);

        return "Successfully roles updated!";
    }

    @Override
    @CacheEvict(value = "users", key = "#email")
    public String deleteUser(String email) throws UserNotFoundException {
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with email : '%s' not found", email));
        }
        User user = optionalUser.get();
        UserDetails userDetails = GetPrincipal.principal();
        if (userDetails.getUsername().equals(email)){
            throw new RuntimeException(String.format("User cannot delete same User, [%s : %s]",
                    userDetails.getUsername(), user.getUserName()));
        }
        userRepository.delete(user);
        return String.format("Successfully deleted user '%s'", user.getUserName());
    }
}
