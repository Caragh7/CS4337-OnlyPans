package com.example.onlypans.onlypans.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @GetMapping("/oauth-token")
    public ResponseEntity<String> oauthToken(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        // Get the access token
        String token = authorizedClient.getAccessToken().getTokenValue();
        // Return the token in the response
        return ResponseEntity.ok(token);
    }
}
