package org.example.gestiontaches.controllers;

import org.example.gestiontaches.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class PrivateController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/tasks")
    public String adminTasks() {
        return roleService.adminAction();
    }
}
