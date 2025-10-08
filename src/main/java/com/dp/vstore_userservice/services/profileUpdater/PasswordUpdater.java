package com.dp.vstore_userservice.services.profileUpdater;

import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUpdater implements ProfileUpdater{
    private final PasswordEncoder passwordEncoder;

    public PasswordUpdater(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void update(User user, UpdateProfileDto dto) {
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }
}
