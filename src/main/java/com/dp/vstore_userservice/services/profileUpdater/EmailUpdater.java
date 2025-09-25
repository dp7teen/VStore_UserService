package com.dp.vstore_userservice.services.profileUpdater;

import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class EmailUpdater implements ProfileUpdater{
    @Override
    public void update(User user, UpdateProfileDto dto) {
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
    }
}
