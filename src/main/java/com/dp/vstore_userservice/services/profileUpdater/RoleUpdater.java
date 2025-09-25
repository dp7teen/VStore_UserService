package com.dp.vstore_userservice.services.profileUpdater;

import com.dp.vstore_userservice.dtos.RoleUpdateDto;
import com.dp.vstore_userservice.models.Role;
import com.dp.vstore_userservice.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleUpdater{
    public void update(User user, RoleUpdateDto dto) {
        if (!dto.getRoles().isEmpty()) {
            List<Role> roles = dto.getRoles().stream()
                    .map(Role::valueOf)
                    .toList();
            user.setRole(roles);
        }
    }
}
