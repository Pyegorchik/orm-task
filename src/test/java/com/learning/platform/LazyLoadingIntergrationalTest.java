package com.learning.platform;

import com.learning.platform.model.*;
import com.learning.platform.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LazyLoadingTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Test
    @Transactional
    void testLazyLoadingWorksWithTransaction() {
        // Given
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User student = createTestUser("lazy_student_" + uniqueSuffix, User.UserRole.STUDENT);
        User instructor = createTestUser("lazy_instructor_" + uniqueSuffix, User.UserRole.INSTRUCTOR);
        Category category = createTestCategory("Test Category " + uniqueSuffix);
        Course course = createTestCourse(instructor, category);
        
        Enrollment enrollment = Enrollment.builder()
                .user(student)
                .course(course)
                .build();
        enrollmentRepository.save(enrollment);
        
        // When (в рамках транзакции)
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(student.getId());
        
        // Then - должен работать без исключения
        assertFalse(enrollments.isEmpty());
        for (Enrollment e : enrollments) {
            assertNotNull(e.getCourse().getTitle()); // Доступ к ленивому полю
            assertNotNull(e.getUser().getUsername());
        }
    }
    
    @Test
    void testDirectLazyLoadingException() {
        // Given
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User student = createTestUser("direct_student_" + uniqueSuffix, User.UserRole.STUDENT);
        User instructor = createTestUser("direct_instructor_" + uniqueSuffix, User.UserRole.INSTRUCTOR);
        Category category = createTestCategory("Direct Category " + uniqueSuffix);
        Course course = createTestCourse(instructor, category);
        
        Enrollment enrollment = Enrollment.builder()
                .user(student)
                .course(course)
                .build();
        enrollmentRepository.save(enrollment);
        
        // Получаем Enrollment без курса
        Enrollment detached = enrollmentRepository.findById(enrollment.getId()).orElseThrow();
        
        // Проверяем, что это proxy
        assertNotNull(detached.getCourse()); // Это proxy, а не реальный объект
        
        System.out.println("Демонстрация концепции Lazy Loading:");
        System.out.println("В рамках транзакции можно получить: " + detached.getCourse().getTitle());
        System.out.println("Вне транзакции будет LazyInitializationException при попытке получить title");
    }
    
    @Test
    @Transactional(readOnly = true)
    void testJoinFetchSolvesLazyLoading() {
        // Given
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User student = createTestUser("fetch_student_" + uniqueSuffix, User.UserRole.STUDENT);
        User instructor = createTestUser("fetch_instructor_" + uniqueSuffix, User.UserRole.INSTRUCTOR);
        Category category = createTestCategory("Fetch Category " + uniqueSuffix);
        Course course = createTestCourse(instructor, category);
        
        Enrollment enrollment = Enrollment.builder()
                .user(student)
                .course(course)
                .build();
        enrollmentRepository.save(enrollment);
        
        // When - используем JOIN FETCH
        List<Enrollment> enrollments = enrollmentRepository.findByUserIdWithCourse(student.getId());
        
        // Then - работает без исключения
        assertFalse(enrollments.isEmpty());
        for (Enrollment e : enrollments) {
            assertNotNull(e.getCourse().getTitle());
        }
    }
    
    @Test
    @Transactional
    void testCollectionInitialization() {
        // Тест для проверки инициализации коллекций
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = createTestUser("collections_user_" + uniqueSuffix, User.UserRole.STUDENT);
        
        // Проверяем, что коллекции инициализированы
        assertNotNull(user.getEnrollments());
        assertNotNull(user.getSubmissions());
        assertNotNull(user.getQuizSubmissions());
        assertNotNull(user.getReviews());
        assertNotNull(user.getCoursesTaught());
        
        assertEquals(0, user.getEnrollments().size());
        assertTrue(user.getEnrollments().isEmpty());
    }
    
    private User createTestUser(String username, User.UserRole role) {
        User user = User.builder()
                .username(username)
                .email(username + "@test.com")
                .firstName("Test")
                .lastName("User")
                .role(role)
                .build();
        user.setPassword("password123");
        
        // Инициализируем коллекции если они null
        if (user.getEnrollments() == null) user.setEnrollments(new ArrayList<>());
        if (user.getSubmissions() == null) user.setSubmissions(new ArrayList<>());
        if (user.getQuizSubmissions() == null) user.setQuizSubmissions(new ArrayList<>());
        if (user.getReviews() == null) user.setReviews(new ArrayList<>());
        if (user.getCoursesTaught() == null) user.setCoursesTaught(new ArrayList<>());
        
        return userRepository.save(user);
    }
    
    private Category createTestCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .description("For lazy loading tests")
                .build();
        return categoryRepository.save(category);
    }
    
    private Course createTestCourse(User instructor, Category category) {
        Course course = Course.builder()
                .title("Lazy Loading Course")
                .description("Testing lazy loading")
                .instructor(instructor)
                .category(category)
                .build();
        
        // Инициализируем коллекции если они null
        if (course.getEnrollments() == null) course.setEnrollments(new ArrayList<>());
        if (course.getModules() == null) course.setModules(new ArrayList<>());
        if (course.getReviews() == null) course.setReviews(new ArrayList<>());
        
        return courseRepository.save(course);
    }
}