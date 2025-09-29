package com.dp.vstore_userservice.services;

import com.dp.vstore_userservice.dtos.RoleUpdateDto;
import com.dp.vstore_userservice.dtos.SignupRequestDto;
import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.exceptions.UserAlreadyPresentException;
import com.dp.vstore_userservice.exceptions.UserNotFoundException;
import com.dp.vstore_userservice.models.Role;
import com.dp.vstore_userservice.models.User;
import com.dp.vstore_userservice.repositories.UserRepository;
import com.dp.vstore_userservice.security.services.JWTHelper;
import com.dp.vstore_userservice.services.profileUpdater.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private List<ProfileUpdater> profileUpdaters;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JWTHelper jwthelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwthelper = jwthelper;
    }

    private Optional<User> findUser(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email);
    }

    private UserDetails getPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (UserDetails) securityContext.getAuthentication().getPrincipal();
    }

    @Override
    public User signup(String userName, String email, String password, List<String> roles) throws UserNotFoundException,
            UserAlreadyPresentException {
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyPresentException(String.format("User already present with this email '%s'", email));
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
    public String login(String email, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        return jwthelper.generateToken(email, claims);
    }

    @Override
    public User me() throws UserNotFoundException {
        UserDetails userDetails = getPrincipal();
        return findUser(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException(String.format("User with email : '%s' not found", userDetails.getUsername())));
    }

    @Override
    public User updateProfile(UpdateProfileDto dto) throws UserNotFoundException {
        UserDetails userDetails = getPrincipal();
        Optional<User> optionalUser = findUser(userDetails.getUsername());
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with email : '%s' not found", userDetails.getUsername()));
        }
        User user = optionalUser.get();
        initiateProfileUpdaters();
        for (ProfileUpdater profileUpdater : profileUpdaters) {
            profileUpdater.update(user, dto);
        }
        return userRepository.save(user);
    }

    @Override
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

    private void initiateProfileUpdaters() {
        profileUpdaters = List.of(
                new EmailUpdater(), new UsernameUpdater(),
                new PasswordUpdater()
        );
    }

    @Override
    public String deleteUser(String email) throws UserNotFoundException {
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(String.format("User with email '%s' not found", email));
        }
        User user = optionalUser.get();
        UserDetails userDetails = getPrincipal();
        if (userDetails.getUsername().equals(email)) {
            throw new RuntimeException(String.format("User with email '%s' cannot be deleted by same user", email));
        }
        userRepository.delete(user);
        return "Successfully deleted!";
    }
}
