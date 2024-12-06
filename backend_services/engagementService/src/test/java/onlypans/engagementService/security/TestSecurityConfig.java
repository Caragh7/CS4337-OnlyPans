package onlypans.engagementService.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    public JwtDecoder jwtDecoder() {
        return jwt -> null;
    }
}
