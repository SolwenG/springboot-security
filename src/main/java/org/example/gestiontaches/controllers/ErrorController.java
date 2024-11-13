package org.example.gestiontaches.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/accessDenied")
    public String errorMessage() {
        return "accessDenied";
    }
}