package ru.ilonich.roswarcp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Илоныч on 28.04.2017.
 */
@Controller
public class MainController {
    @GetMapping("/")
    public String root() {
        return "redirect:console";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/console")
    public String console() {
        return "console";
    }
}
