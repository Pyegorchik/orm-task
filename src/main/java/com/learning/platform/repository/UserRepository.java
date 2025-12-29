package com.learning.platform.repository;

import com.learning.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    List<User> findByRole(User.UserRole role);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.coursesTaught WHERE u.id = :id")
    Optional<User> findByIdWithCourses(@Param("id") Long id);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.enrollments WHERE u.id = :id")
    Optional<User> findByIdWithEnrollments(@Param("id") Long id);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.role = 'INSTRUCTOR'")
    List<User> findAllInstructors();
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.enrollments e LEFT JOIN FETCH e.course WHERE u.id = :id")
    Optional<User> findByIdWithEnrollmentsAndCourses(@Param("id") Long id);
}