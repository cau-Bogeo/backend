package com.caubogeo.bogeo.dto.jwt;

import lombok.Data;

@Data
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
