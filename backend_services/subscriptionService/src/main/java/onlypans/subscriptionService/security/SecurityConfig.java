package onlypans.subscriptionService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    String[] ignorePatterns = {"/actuator/**", "/subscriptions/webhook"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers(ignorePatterns);
                })
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ignorePatterns).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
