package onlypans.mediaService.controller;

import onlypans.mediaService.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam("fileName") String fileName, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Authentication failed.", HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName();
        System.out.println("Presigned URL requested: " + username);

        try {
            String url = mediaService.generatePresignedUrl(fileName);
            return new ResponseEntity<>(url, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}