package com.example.jwtdemo.model;

// AuthenticationResponse class
public class AuthenticationResponse {
    private final String accessToken;
    private final String refreshToken;
    private final Long expiresTime;
    private final Long refreshExpiresTime;

    public AuthenticationResponse(String accessToken, String refreshToken, Long expiresTime,Long refreshExpiresTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresTime = expiresTime;
        this.refreshExpiresTime = refreshExpiresTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresTime(){
        return expiresTime;
    }

    public Long getRefreshExpiresTime(){
        return refreshExpiresTime;
    }
}
