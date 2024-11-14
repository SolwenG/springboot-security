package org.example.gestiontaches.services;

import org.example.gestiontaches.models.Task;
import org.example.gestiontaches.models.Users;
import org.example.gestiontaches.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    public List<Task> findTasksByUserId(Users userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        Optional<Task> taskBdd = taskRepository.findById(id);
        if (taskBdd.isPresent()) {
            Task newTask = taskBdd.get();
            newTask.setTitle(task.getTitle());
            newTask.setDescription(task.getDescription());
            newTask.setStatus(task.getStatus());
            return taskRepository.save(newTask);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
