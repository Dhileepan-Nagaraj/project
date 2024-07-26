package com.web10.taskmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web10.taskmanagement.entity.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

    // Find tasks by assigned user ID
    List<Task> findByAssignedUserId(Integer userId);

    // Find tasks by status
    List<Task> findByStatus(Task.Status status);

    // Find tasks by priority
    List<Task> findByPriority(Task.Priority priority);

    // Find tasks due between two dates
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksDueBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find tasks with a specific status and assigned user
    @Query("SELECT t FROM Task t WHERE t.status = :status AND t.assignedUser.id = :userId")
    List<Task> findTasksByStatusAndUser(@Param("status") Task.Status status, @Param("userId") Integer userId);

    // Paginated and sorted tasks by status
    @Query("SELECT t FROM Task t WHERE t.status = :status")
    Page<Task> findTasksByStatusWithPagination(@Param("status") Task.Status status, Pageable pageable);

    // Find tasks with description containing a keyword
    @Query("SELECT t FROM Task t WHERE t.description LIKE :keyword")
    List<Task> searchTasksByDescriptionKeyword(@Param("keyword") String keyword);

    // Find tasks sorted by creation date in descending order
    @Query("SELECT t FROM Task t ORDER BY t.createdAt DESC")
    List<Task> findAllTasksOrderByCreationDateDesc();

    // Find tasks by priority using a native SQL query
    @Query(value = "SELECT * FROM tasks t WHERE t.priority = :priority", nativeQuery = true)
    List<Task> findTasksByPriorityNative(@Param("priority") String priority);

    // Update the status of a task
    @Modifying
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :id")
    void updateTaskStatus(@Param("id") Integer id, @Param("status") Task.Status status);

    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}
