package com.web10.taskmanagement.controllers;

import com.web10.taskmanagement.entity.Task;
import com.web10.taskmanagement.exceptions.TaskNotFoundException;
import com.web10.taskmanagement.exceptions.UserNotFoundException;
import com.web10.taskmanagement.services.TaskService;
import com.web10.taskmanagement.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> priority,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortDir,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        try {
            String sortField = sortBy.orElse("title");
            String sortDirection = sortDir.orElse("asc");
            Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10), Sort.by(direction, sortField));
            Page<Task> tasks = taskService.getTasks(status, priority, pageable);

            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error fetching tasks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        logger.info("Received request to create task: {}", task);
        try {
            Task createdTask = taskService.createTask(task);
            logger.info("Task created successfully: {}", createdTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            logger.error("Error creating task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @Valid @RequestBody Task task) {
        try {
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception e) {
            logger.error("Error deleting task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }



    @PutMapping("/assign/{taskId}/{userId}")
    public ResponseEntity<Task> assignTask(@PathVariable Integer taskId, @PathVariable Integer userId) {
        try {
            Task updatedTask = taskService.assignTask(taskId, userId);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error assigning task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
