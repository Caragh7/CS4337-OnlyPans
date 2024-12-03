package onlypans.emailNotificationService.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import onlypans.emailNotificationService.dto.EmailDetailDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromUsr;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        // Sends an email
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setFrom(fromUsr);
        helper.setSubject(subject);
        helper.setText(body, true);
        emailSender.send(message);
    }
    // listen to email_queue
    @RabbitListener(queues = "email.queue")
    public void processEmailMessage(EmailDetailDTO emailDetailDTO) throws MessagingException {
        // extract email
        String to = emailDetailDTO.getTo();
        String subject = emailDetailDTO.getSubject();
        String body = generateEmailBody(emailDetailDTO);
        sendEmail(to, subject, body);
    }

    public String generateEmailBody(EmailDetailDTO emailDetailDTO) {
        // generate the email body based on the provided template
        String templateName = emailDetailDTO.getTemplateName();
        String template = loadEmailTemplate(templateName);

        String body = template;
        if (emailDetailDTO.getDynamicValue() != null) {
            for (Map.Entry<String, Object> entry : emailDetailDTO.getDynamicValue().entrySet()) {
                body = body.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
            }
        }

        return body;
    }

    public String loadEmailTemplate(String templateName) {
        // Load a template
        ClassPathResource resource = new ClassPathResource("templates/emails/" + templateName + ".html");
        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error loading email template " + templateName, e);
        }
    }
}
