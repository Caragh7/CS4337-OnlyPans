package onlypans.api_gateway.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@ConfigurationProperties(prefix = "jwt.converter")
public class JWTConverterProperties {

    /**
     * The principal attribute name in the JWT token.
     */
    private String principalAttribute = "sub";  // Default to 'sub', can be overridden in application properties

    /**
     * The resource ID used to look up roles in the JWT token.
     */
    private String resourceId;

    public String getPrincipalAttribute() {
        return principalAttribute;
    }

    public void setPrincipalAttribute(String principalAttribute) {
        this.principalAttribute = principalAttribute;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
