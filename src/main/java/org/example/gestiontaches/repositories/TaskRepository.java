package org.example.gestiontaches.repositories;

import org.example.gestiontaches.models.Task;
import org.example.gestiontaches.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Users userId);
}
