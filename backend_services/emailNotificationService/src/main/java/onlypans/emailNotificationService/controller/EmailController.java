package onlypans.emailNotificationService.controller;

import lombok.RequiredArgsConstructor;
import onlypans.emailNotificationService.dto.EmailDetailDTO;
import onlypans.emailNotificationService.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDetailDTO emailDetailDTO) {
        try {
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

