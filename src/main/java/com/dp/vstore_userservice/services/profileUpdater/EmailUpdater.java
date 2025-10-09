package com.dp.vstore_userservice.services.profileUpdater;

import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.models.User;
import com.dp.vstore_userservice.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class EmailUpdater implements ProfileUpdater{
    private final UserRepository userRepository;

    public EmailUpdater(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void update(User user, UpdateProfileDto dto) {
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Bad Credentials!");
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
    }
}
