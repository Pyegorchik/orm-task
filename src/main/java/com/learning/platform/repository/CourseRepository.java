package com.learning.platform.repository;

import com.learning.platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategoryId(Long categoryId);
    List<Course> findByInstructorId(Long instructorId);
    List<Course> findByIsPublishedTrue();
    
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.category WHERE c.id = :id")
    Optional<Course> findByIdWithCategory(@Param("id") Long id);
    
    @Query("SELECT c FROM Course c JOIN FETCH c.instructor WHERE c.id = :id")
    Optional<Course> findByIdWithInstructor(@Param("id") Long id);
    
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.category LEFT JOIN FETCH c.instructor WHERE c.id = :id")
    Optional<Course> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT c FROM Course c WHERE c.level = :level AND c.isPublished = true")
    List<Course> findByLevelAndPublished(@Param("level") Course.CourseLevel level);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.instructor.id = :instructorId")
    Long countByInstructorId(@Param("instructorId") Long instructorId);
}