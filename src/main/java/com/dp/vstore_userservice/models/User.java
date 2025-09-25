package com.dp.vstore_userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel{
    private String userName;
    private String email;
    private String password;
    @Enumerated(EnumType.ORDINAL)
    private List<Role> role;
}
