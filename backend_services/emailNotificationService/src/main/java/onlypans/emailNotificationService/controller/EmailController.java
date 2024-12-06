package onlypans.emailNotificationService.controller;

import lombok.RequiredArgsConstructor;
import onlypans.emailNotificationService.dto.EmailDetailDTO;
import onlypans.emailNotificationService.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDetailDTO emailDetailDTO, Authentication authentication) {
        try {
            // Get the authenticated user's email from the token
            String userEmail = null;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                Map<String, Object> claims = jwtAuth.getTokenAttributes();
                userEmail = (String) claims.get("email"); // Extract email from the claims
            }

            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Failed to extract user email from authentication.");
            }

            System.out.println("User Email: " + userEmail);
            emailDetailDTO.setTo(userEmail);

            // Process the email message and send it
            emailService.processEmailMessage(emailDetailDTO);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Email Notification Service is running!");
    }
}
