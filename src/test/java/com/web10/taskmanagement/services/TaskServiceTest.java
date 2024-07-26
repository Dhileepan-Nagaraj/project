package com.web10.taskmanagement.services;

import com.web10.taskmanagement.entity.Task;
import com.web10.taskmanagement.repository.TaskRepository;
import com.web10.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask() {
        // Create a valid task
        Task task = new Task();
        task.setTitle("Valid Title");
        task.setDueDate(LocalDate.now().plusDays(1));

        // Mock the save method
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Call the service method
        Task createdTask = taskService.createTask(task);

        // Verify the save method was called
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask() {
        // Create a valid task
        Task task = new Task();
        task.setId(1);
        task.setTitle("Updated Title");
        task.setDueDate(LocalDate.now().plusDays(1));

        // Mock the existsById and save methods
        when(taskRepository.existsById(anyInt())).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Call the service method
        Task updatedTask = taskService.updateTask(1, task);

        // Verify the save method was called
        verify(taskRepository, times(1)).save(any(Task.class));
    }
}
