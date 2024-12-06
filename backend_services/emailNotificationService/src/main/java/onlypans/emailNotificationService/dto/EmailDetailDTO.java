package onlypans.emailNotificationService.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EmailDetailDTO {
    // Defines the structure of the email being sent
    private String to;
    private String subject;
    // will replace placeholders with values
    private Map<String, Object> dynamicValue;
    private String templateName;
}
