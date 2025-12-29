package com.learning.platform.repository;

import com.learning.platform.model.Enrollment;
import com.learning.platform.model.User;
import com.learning.platform.model.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByCourseId(Long courseId);
    boolean existsByUserAndCourse(User user, Course course);
    // Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    List<Enrollment> findByStatus(Enrollment.EnrollmentStatus status);
    // List<Enrollment> findByStudentId(Long studentId);

    // Добавляем метод с JOIN FETCH
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.user.id = :userId")
    List<Enrollment> findByUserIdWithCourse(@Param("userId") Long userId);
    
    // Метод с JOIN FETCH для курса и пользователя
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.user WHERE e.id = :id")
    Optional<Enrollment> findByIdWithCourseAndUser(@Param("id") Long id);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByUserId(@Param("userId") Long userId);
}


