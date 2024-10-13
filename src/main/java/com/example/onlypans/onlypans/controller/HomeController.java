package com.example.onlypans.onlypans.controller;

import com.example.onlypans.onlypans.utils.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
