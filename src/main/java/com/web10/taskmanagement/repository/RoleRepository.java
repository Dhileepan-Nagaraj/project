package com.web10.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.web10.taskmanagement.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Find role by name
    Optional<Role> findByName(String name);
    default Role getDefaultRole() {
        return findByName("USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("USER");
            return save(role);
        });
    }

    // Custom query to find roles by a partial name match (case insensitive)
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Role> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find all roles except a given role by name
    @Query("SELECT r FROM Role r WHERE r.name <> :name")
    List<Role> findAllExcept(@Param("name") String name);

    // Find roles with IDs in a given list
    @Query("SELECT r FROM Role r WHERE r.id IN :ids")
    List<Role> findByIds(@Param("ids") List<Long> ids);

    // Count roles with a given name
    @Query("SELECT COUNT(r) FROM Role r WHERE r.name = :name")
    long countByName(@Param("name") String name);
}