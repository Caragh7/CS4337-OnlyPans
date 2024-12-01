package onlypans.emailNotificationService.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class EmailController {
    @GetMapping("/health-check")
    public String healthCheck() {
        return "Notification Service is running!";
    }
}
