package com.dp.vstore_userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private String token;

    public static TokenDto from(String token) {
        TokenDto dto = new TokenDto();
        dto.setToken(token);
        return dto;
    }
}
