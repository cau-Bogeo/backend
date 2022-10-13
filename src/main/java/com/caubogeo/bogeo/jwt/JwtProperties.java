package com.caubogeo.bogeo.jwt;

public class JwtProperties {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String SECRET = "aGVsbG8tZXZlcnlvbmUtdGhpcy1hcHAtaXMtYm9nZW8tdGhhbmsteW91";
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 604800000L * 112; // 4달
}
