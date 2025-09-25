package com.dp.vstore_userservice.dtos;

import com.dp.vstore_userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private List<String> roles;

    public static UserDto from (User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUserName());
        userDto.setRoles(new ArrayList<>());
        user.getRole().forEach(role -> userDto.getRoles().add(role.name()));
        return userDto;
    }
}
