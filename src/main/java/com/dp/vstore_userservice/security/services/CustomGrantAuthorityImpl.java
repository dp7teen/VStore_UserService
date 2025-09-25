package com.dp.vstore_userservice.security.services;

import com.dp.vstore_userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class CustomGrantAuthorityImpl implements GrantedAuthority {
    private String role;

    public CustomGrantAuthorityImpl() {
    }

    public CustomGrantAuthorityImpl(Role role) {
        this.role = role.name();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
