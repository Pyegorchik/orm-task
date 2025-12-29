package com.learning.platform.service;

import com.learning.platform.model.*;
import com.learning.platform.model.Module;
import com.learning.platform.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "com.learning.platform")
@ActiveProfiles("test")
@Transactional
class CourseServiceIntegrationTest {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ModuleRepository moduleRepository;
    
    private User instructor;
    private Category category;
    
    @BeforeEach
    void setUp() {
        // Создаем тестовые данные
        instructor = User.builder()
                .username("test_instructor")
                .email("instructor@test.com")
                .firstName("John")
                .lastName("Doe")
                .role(User.UserRole.INSTRUCTOR)
                .build();
        instructor.setPassword("password123");
        userRepository.save(instructor);
        
        category = Category.builder()
                .name("Programming")
                .description("Programming courses")
                .build();
        categoryRepository.save(category);
    }
    
    @Test
    void testCreateCourse() {
        // Given
        Course course = Course.builder()
                .title("Java Programming")
                .description("Learn Java from scratch")
                .duration(40)
                .level(Course.CourseLevel.BEGINNER)
                .price(99.99)
                .startDate(LocalDate.now())
                .instructor(instructor)
                .category(category)
                .build();
        
        // When
        Course savedCourse = courseService.createCourse(course);
        
        // Then
        assertNotNull(savedCourse.getId());
        assertEquals("Java Programming", savedCourse.getTitle());
        assertEquals(instructor, savedCourse.getInstructor());
        assertEquals(category, savedCourse.getCategory());
        assertFalse(savedCourse.getIsPublished());
    }
    
    @Test
    void testGetCourseById() {
        // Given
        Course course = Course.builder()
                .title("Test Course")
                .description("Test Description")
                .instructor(instructor)
                .category(category)
                .build();
        Course savedCourse = courseRepository.save(course);
        
        // When
        Course foundCourse = courseService.getCourseById(savedCourse.getId());
        
        // Then
        assertNotNull(foundCourse);
        assertEquals(savedCourse.getId(), foundCourse.getId());
        assertEquals("Test Course", foundCourse.getTitle());
    }
    
    @Test
    void testUpdateCourse() {
        // Given
        Course course = Course.builder()
                .title("Old Title")
                .description("Old Description")
                .instructor(instructor)
                .category(category)
                .build();
        Course savedCourse = courseRepository.save(course);
        
        // When
        Course updateData = Course.builder()
                .title("New Title")
                .description("New Description")
                .duration(30)
                .level(Course.CourseLevel.INTERMEDIATE)
                .build();
        
        Course updatedCourse = courseService.updateCourse(savedCourse.getId(), updateData);
        
        // Then
        assertEquals("New Title", updatedCourse.getTitle());
        assertEquals("New Description", updatedCourse.getDescription());
        assertEquals(30, updatedCourse.getDuration());
        assertEquals(Course.CourseLevel.INTERMEDIATE, updatedCourse.getLevel());
    }
    
    @Test
    void testDeleteCourse() {
        // Given
        Course course = Course.builder()
                .title("Course to Delete")
                .description("Will be deleted")
                .instructor(instructor)
                .category(category)
                .build();
        Course savedCourse = courseRepository.save(course);
        
        // When
        courseService.deleteCourse(savedCourse.getId());
        
        // Then
        assertThrows(RuntimeException.class, () -> {
            courseService.getCourseById(savedCourse.getId());
        });
    }
    
    @Test
    void testAddModuleToCourse() {
        // Given
        Course course = Course.builder()
                .title("Course with Modules")
                .description("Test")
                .instructor(instructor)
                .category(category)
                .build();
        Course savedCourse = courseRepository.save(course);
        
        Module module = Module.builder()
                .title("Module 1")
                .description("First module")
                .orderIndex(1)
                .course(savedCourse)
                .build();
        
        // When
        Module savedModule = moduleRepository.save(module);
        
        // Then
        assertNotNull(savedModule.getId());
        assertEquals("Module 1", savedModule.getTitle());
        assertEquals(savedCourse, savedModule.getCourse());
        
        List<Module> modules = moduleRepository.findByCourseId(savedCourse.getId());
        assertEquals(1, modules.size());
    }
    
    @Test
    void testGetCoursesByCategory() {
        // Given
        Course course1 = Course.builder()
                .title("Java Course")
                .description("Java")
                .instructor(instructor)
                .category(category)
                .build();
        
        Course course2 = Course.builder()
                .title("Advanced Java")
                .description("Advanced")
                .instructor(instructor)
                .category(category)
                .build();
        
        courseRepository.saveAll(List.of(course1, course2));
        
        // When
        List<Course> courses = courseService.getCoursesByCategory(category.getId());
        
        // Then
        assertEquals(2, courses.size());
        assertTrue(courses.stream().allMatch(c -> c.getCategory().equals(category)));
    }
}