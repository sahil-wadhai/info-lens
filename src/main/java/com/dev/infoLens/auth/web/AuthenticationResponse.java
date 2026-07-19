package com.dev.infoLens.auth.web;


@lombok.Data
@lombok.AllArgsConstructor
public class AuthenticationResponse {
    private String status;       // Populated with "refreshed"
    private String accessToken;  // Populated with your new token string
}

