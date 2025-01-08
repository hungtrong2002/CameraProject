package com.camera.modelAPI;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResult {
    @JsonProperty("AccessToken")
    private String accessToken;

    @JsonProperty("TokenType")
    private String tokenType = "Bearer";

    @JsonProperty("IdToken")
    private String idToken;

    @JsonProperty("RefreshToken")
    private String refreshToken;

    @JsonProperty("ExpiresIn")
    private int expiresIn;

    public AuthenticationResult(String accessToken, String idToken, String refreshToken, int expiresIn) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
