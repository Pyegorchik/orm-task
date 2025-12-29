package com.learning.platform.repository;

import com.learning.platform.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByLessonId(Long lessonId);
    
    @Query("SELECT a FROM Assignment a WHERE a.lesson.module.course.id = :courseId")
    List<Assignment> findByCourseId(@Param("courseId") Long courseId);

    List<Assignment> findByDueDateBefore(LocalDateTime dueDate);
    
    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.submissions WHERE a.id = :id")
    Optional<Assignment> findByIdWithSubmissions(@Param("id") Long id);
}
