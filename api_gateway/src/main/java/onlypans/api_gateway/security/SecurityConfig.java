package onlypans.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        return JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri());
    }

    public static final String ADMIN = "admin";
    public static final String USER = "user";

    private final JWTConverter jwtConverter;

    public SecurityConfig(JWTConverter jwtConverter) {
        this.jwtConverter = jwtConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/metrics", "/consul/**") // Permit these without token
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/subscriptions/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/user/**").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/api/admin-and-user/**").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("http://localhost:8080/realms/onlypans/protocol/openid-connect/auth?client_id=onlypans-client&redirect_uri=http://localhost:8080&response_type=code");
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("http://localhost:8080/realms/onlypans/protocol/openid-connect/logout")
                );

        return http.build();
    }
}