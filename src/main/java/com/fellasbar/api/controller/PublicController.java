package com.fellasbar.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/thebarsapp")
    public String barsApp() {
        return "thebarsapp";
    }

    @GetMapping("/vlogs")
    public String vlogs() {
        return "vlogs";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @GetMapping("/store")
    public String store() {
        return "store";
    }
}
