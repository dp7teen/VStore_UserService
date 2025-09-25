package com.dp.vstore_userservice.services.profileUpdater;

import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UsernameUpdater implements ProfileUpdater {
    @Override
    public void update(User user, UpdateProfileDto dto) {
        if (dto.getUsername() != null) {
            user.setUserName(dto.getUsername());
        }
    }
}
