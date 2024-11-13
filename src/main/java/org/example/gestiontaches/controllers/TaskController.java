package org.example.gestiontaches.controllers;

import org.example.gestiontaches.models.Task;
import org.example.gestiontaches.models.Users;
import org.example.gestiontaches.services.TaskService;
import org.example.gestiontaches.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        if (user != null) {
            List<Task> userTasks = taskService.findTasksByUserId(user);
            return ResponseEntity.ok(userTasks);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Task task = taskService.findTaskById(id);

        if (task != null && (task.getUserId().getUsername().equals(username) || isAdmin(authentication))) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        if (user != null) {
            task.setUserId(user);
            task.setStatus(task.getStatus() != null ? task.getStatus() : Task.Status.A_FAIRE); // Default if status isn't provided
            return ResponseEntity.ok(taskService.createTask(task));
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/create")
    public String showCreateTaskForm() {
        return "createTask";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task existingTask = taskService.findTaskById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (existingTask != null && (existingTask.getUserId().getUsername().equals(username) || isAdmin(authentication))) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            return ResponseEntity.ok(taskService.updateTask(existingTask));
        }
        return ResponseEntity.status(403).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task task = taskService.findTaskById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (task != null && (task.getUserId().getUsername().equals(username) || isAdmin(authentication))) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
