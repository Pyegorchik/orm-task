package com.learning.platform.service;

import com.learning.platform.model.*;
import com.learning.platform.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "com.learning.platform.service")
@ActiveProfiles("test")
@Transactional
class EnrollmentServiceIntegrationTest {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    private User student;
    private User instructor;
    private Course course;
    
    @BeforeEach
    void setUp() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        
        // Create student с уникальным именем
        student = User.builder()
                .username("test_student_" + uniqueSuffix)
                .email("student" + uniqueSuffix + "@test.com")
                .firstName("Student")
                .lastName("Test")
                .role(User.UserRole.STUDENT)
                .build();
        student.setPassword("password123");
        
        // Инициализируем коллекции
        if (student.getEnrollments() == null) student.setEnrollments(new ArrayList<>());
        if (student.getSubmissions() == null) student.setSubmissions(new ArrayList<>());
        if (student.getQuizSubmissions() == null) student.setQuizSubmissions(new ArrayList<>());
        if (student.getReviews() == null) student.setReviews(new ArrayList<>());
        userRepository.save(student);
        
        // Create instructor с уникальным именем
        instructor = User.builder()
                .username("test_teacher_" + uniqueSuffix)
                .email("teacher" + uniqueSuffix + "@test.com")
                .firstName("Teacher")
                .lastName("Test")
                .role(User.UserRole.INSTRUCTOR)
                .build();
        instructor.setPassword("password123");
        if (instructor.getCoursesTaught() == null) instructor.setCoursesTaught(new ArrayList<>());
        userRepository.save(instructor);
        
        // Create category с уникальным именем
        Category category = Category.builder()
                .name("Test Category " + uniqueSuffix)
                .description("Test")
                .build();
        categoryRepository.save(category);
        
        // Create course
        course = Course.builder()
                .title("Test Course " + uniqueSuffix)
                .description("Test Description")
                .instructor(instructor)
                .category(category)
                .build();
        if (course.getEnrollments() == null) course.setEnrollments(new ArrayList<>());
        if (course.getModules() == null) course.setModules(new ArrayList<>());
        if (course.getReviews() == null) course.setReviews(new ArrayList<>());
        courseRepository.save(course);
    }
    
    @Test
    void testEnrollUser() {
        // When
        Enrollment enrollment = enrollmentService.enrollUser(student.getId(), course.getId());
        
        // Then
        assertNotNull(enrollment.getId());
        assertEquals(student.getId(), enrollment.getUser().getId());
        assertEquals(course.getId(), enrollment.getCourse().getId());
        assertEquals(Enrollment.EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertNotNull(enrollment.getEnrollDate());
    }
     
    @Test
    void testEnrollUserAlreadyEnrolled() {
        // Given
        enrollmentService.enrollUser(student.getId(), course.getId());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollUser(student.getId(), course.getId());
        });
    }
    
    @Test
    void testGetUserEnrollmentsWithTransactional() {
        // Given
        enrollmentService.enrollUser(student.getId(), course.getId());
        
        // When
        List<Enrollment> enrollments = enrollmentService.getUserEnrollmentsWithTransactional(student.getId());
        
        // Then
        assertEquals(1, enrollments.size());
        assertEquals(course.getTitle(), enrollments.get(0).getCourse().getTitle());
    }
    
    @Test
    void testCompleteEnrollment() {
        // Given
        Enrollment enrollment = enrollmentService.enrollUser(student.getId(), course.getId());
        
        // When
        Enrollment completed = enrollmentService.completeEnrollment(enrollment.getId());
        
        // Then
        assertEquals(Enrollment.EnrollmentStatus.COMPLETED, completed.getStatus());
    }
    
    @Test
    void testCancelEnrollment() {
        // Given
        Enrollment enrollment = enrollmentService.enrollUser(student.getId(), course.getId());
        
        // When
        Enrollment cancelled = enrollmentService.cancelEnrollment(enrollment.getId());
        
        // Then
        assertEquals(Enrollment.EnrollmentStatus.DROPPED, cancelled.getStatus());
    }
    
    @Test
    void testIsUserEnrolled() {
        // Given
        enrollmentService.enrollUser(student.getId(), course.getId());
        
        // When
        boolean isEnrolled = enrollmentService.isUserEnrolled(student.getId(), course.getId());
        
        // Then
        assertTrue(isEnrolled);
    }
    
    @Test
    void testGetActiveEnrollments() {
        // Given
        enrollmentService.enrollUser(student.getId(), course.getId());
        
        // When
        List<Enrollment> activeEnrollments = enrollmentService.getActiveEnrollmentsByUserId(student.getId());
        
        // Then
        assertEquals(1, activeEnrollments.size());
        assertEquals(Enrollment.EnrollmentStatus.ACTIVE, activeEnrollments.get(0).getStatus());
    }
  
}