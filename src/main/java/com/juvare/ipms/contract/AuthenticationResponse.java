package com.juvare.ipms.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expiration_in_minutes")
    private int expirationInMinutes;


    public AuthenticationResponse(String accessToken, int expirationInMinutes){
        this.accessToken = accessToken;
        this.expirationInMinutes = expirationInMinutes;
    }
}
