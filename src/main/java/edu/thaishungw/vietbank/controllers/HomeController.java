package edu.thaishungw.vietbank.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {

            return "login";
        }

        return ("index");
    }

    @RequestMapping("/login")
    public String login() {

        return ("login");
    }
}
