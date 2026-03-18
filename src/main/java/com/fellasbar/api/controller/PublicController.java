package com.fellasbar.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("bodyClass", "coastal");
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
    public String vlogs(Model model) {
        model.addAttribute("bodyClass", "coastal");
        return "vlogs";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("bodyClass", "coastal");
        return "blog";
    }

    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("bodyClass", "coastal");
        return "store";
    }
}
