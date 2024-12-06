package onlypans.emailNotificationService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Patterns to ignore security checks
    private static final String[] IGNORE_PATTERNS = {"/email/health", "/actuator/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers(IGNORE_PATTERNS);
                })
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(IGNORE_PATTERNS).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
