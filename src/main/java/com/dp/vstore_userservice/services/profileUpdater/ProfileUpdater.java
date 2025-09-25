package com.dp.vstore_userservice.services.profileUpdater;

import com.dp.vstore_userservice.dtos.UpdateProfileDto;
import com.dp.vstore_userservice.models.User;
import org.springframework.stereotype.Component;

@Component
public interface ProfileUpdater {
    void update(User user, UpdateProfileDto dto);
}
