package onlypans.authService.controller;

import onlypans.authService.utils.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() throws URISyntaxException, IOException {
        return FileUtils.readResourceString("templates/index.html");
    }

    @GetMapping("/secured")
    public String secured() {
        return "Hello, Secured";
    }
}
