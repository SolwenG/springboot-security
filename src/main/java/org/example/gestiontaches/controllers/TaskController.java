package org.example.gestiontaches.controllers;

import org.example.gestiontaches.models.Task;
import org.example.gestiontaches.models.Users;
import org.example.gestiontaches.services.TaskService;
import org.example.gestiontaches.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // FIND ALL POUR ADMIN
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/tasks")
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.findAllTasks();
        model.addAttribute("tasks", tasks);
        return "adminListTasks";
    }

    // FIND ALL POUR USER
    @GetMapping("/tasks")
    public String getUserTasks(Model model) {
        Users user = getAuthenticatedUser();  // Use the helper method here
        List<Task> tasks = taskService.findTasksByUserId(user);
        model.addAttribute("tasks", tasks);
        return "listTasks";
    }

    // CREATE
    @PostMapping("/tasks")
    public String create(@ModelAttribute("task") Task task) {
        Users user = getAuthenticatedUser();  // Use the helper method here
        task.setUserId(user);
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    // CREATE VUE
    @GetMapping("/tasks/create")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "createTask";
    }

    // UPDATE POUR ADMIN OU LE USER CONNECTE
    @PostMapping("/tasks/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute("task") Task task) {
        Users user = getAuthenticatedUser();  // Use the helper method here

        if (task.getUserId() == null) {
            task.setUserId(user);
        }

        if (task.getUserId().getUsername().equals(user.getUsername()) || isAdmin()) {
            taskService.updateTask(id, task);
        }

        if (isAdmin()) {
            return "redirect:/admin/tasks";
        } else {
            return "redirect:/tasks";
        }
    }

    // UPDATE VUE
    @GetMapping("/tasks/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Task task = taskService.findTaskById(id);
        Users user = getAuthenticatedUser();  // Use the helper method here

        if (task.getUserId() == null) {
            task.setUserId(user);
        }

        model.addAttribute("task", task);
        return "updateTask";
    }

    // DELETE POUR ADMIN
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/tasks/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return "redirect:/admin/tasks";
    }

    // Recupere le username du user connecte
    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    // Check si user est un admin
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}

