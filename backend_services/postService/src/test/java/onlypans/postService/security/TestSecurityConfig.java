package onlypans.postService.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return mockJwt -> Jwt.withTokenValue(mockJwt)
                .header("alg", "none")
                .claim("sub", "user123")
                .claim("given_name", "John")
                .claim("family_name", "Doe")
                .build();
    }
}


