package com.camera.response;

import com.camera.modelAPI.AuthenticationResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {
    @JsonProperty("AuthenticationResult")
    private AuthenticationResult authenticationResult;

    public JwtResponse(AuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }

    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    public void setAuthenticationResult(AuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }
}
