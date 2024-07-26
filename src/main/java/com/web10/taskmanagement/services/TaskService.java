package com.web10.taskmanagement.services;

import com.web10.taskmanagement.entity.Task;
import com.web10.taskmanagement.entity.User;
import com.web10.taskmanagement.exceptions.TaskNotFoundException;
import com.web10.taskmanagement.exceptions.UserNotFoundException;
import com.web10.taskmanagement.repository.TaskRepository;
import com.web10.taskmanagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Task> getTasks(Optional<String> status, Optional<String> priority, Pageable pageable) {
        return taskRepository.findAll((Specification<Task>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            status.ifPresent(s -> predicate.getExpressions().add(criteriaBuilder.equal(root.get("status"), s)));
            priority.ifPresent(p -> predicate.getExpressions().add(criteriaBuilder.equal(root.get("priority"), p)));
            return predicate;
        }, pageable);
    }

    public Task createTask(@Valid Task task) {
        validateTask(task);
        return taskRepository.save(task);
    }

    public Task updateTask(Integer id, @Valid Task task) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        validateTask(task);
        task.setId(id);
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }

    public Task assignTask(Integer taskId, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        task.setAssignedUser(user);
        return taskRepository.save(task);
    }

    private void validateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        if (task.getDueDate() == null) {
            throw new IllegalArgumentException("Task due date cannot be null");
        }
    }
}
