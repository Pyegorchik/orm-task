package com.learning.platform.service;

import com.learning.platform.model.Enrollment;
import com.learning.platform.model.Course;
import com.learning.platform.model.User;
import com.learning.platform.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;
    
    @Transactional
    public Enrollment enrollUser(Long userId, Long courseId) {
        User user = userService.getUserById(userId);
        Course course = courseService.getCourseById(courseId);
        
        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new RuntimeException("User already enrolled in this course");
        }
        
        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .enrollDate(LocalDateTime.now())
                .status(Enrollment.EnrollmentStatus.ACTIVE)
                .build();
        
        user.addEnrollment(enrollment);
        course.addEnrollment(enrollment);
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Transactional
    public Enrollment updateEnrollmentStatus(Long enrollmentId, Enrollment.EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }
    
    // Метод демонстрирует проблему LazyInitializationException
    // если вызвать без @Transactional
    public List<Enrollment> getUserEnrollmentsWithoutTransactional(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        
        log.info("Found {} enrollments for user {}", enrollments.size(), userId);
        
        // Попытка обратиться к ленивой коллекции вызовет LazyInitializationException
        // если метод не помечен @Transactional
        try {
            for (Enrollment enrollment : enrollments) {
                // Доступ к лениво загруженному полю course
                Course course = enrollment.getCourse();
                log.info("Course title: {}", course.getTitle()); // Ошибка может возникнуть здесь!
            }
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
            throw new RuntimeException("LazyInitializationException occurred", e);
        }
        
        return enrollments;
    }
    
    // Правильный подход с @Transactional
    @Transactional(readOnly = true)
    public List<Enrollment> getUserEnrollmentsWithTransactional(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        
        for (Enrollment enrollment : enrollments) {
            // Теперь это работает, потому что мы в рамках транзакции
            Course course = enrollment.getCourse();
            log.info("Course title: {}", course.getTitle());
            log.info("Course duration: {} hours", course.getDuration());
            log.info("Course level: {}", course.getLevel());
            log.info("Course start date: {}", course.getStartDate());
        }
        
        return enrollments;
    }
    
    // Альтернативный подход с использованием JOIN FETCH в запросе
    @Transactional(readOnly = true)
    public List<Enrollment> getUserEnrollmentsWithFetch(Long userId) {
        return enrollmentRepository.findByUserIdWithCourse(userId);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
    
    @Transactional(readOnly = true)
    public List<Enrollment> getActiveEnrollmentsByUserId(Long userId) {
        return enrollmentRepository.findActiveEnrollmentsByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public boolean isUserEnrolled(Long userId, Long courseId) {
        User user = userService.getUserById(userId);
        Course course = courseService.getCourseById(courseId);
        
        return enrollmentRepository.existsByUserAndCourse(user, course);
    }
    
    @Transactional(readOnly = true)
    public Enrollment getEnrollmentById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
    }
    
    @Transactional(readOnly = true)
    public Enrollment getEnrollmentWithDetails(Long enrollmentId) {
        return enrollmentRepository.findByIdWithCourseAndUser(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
    }

    @Transactional
    public Enrollment completeEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setStatus(Enrollment.EnrollmentStatus.COMPLETED);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));
        
        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        return enrollmentRepository.save(enrollment);
    }
}