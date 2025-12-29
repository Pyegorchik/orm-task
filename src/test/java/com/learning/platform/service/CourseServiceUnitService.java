package com.learning.platform.service;

import com.learning.platform.model.Course;
import com.learning.platform.model.User;
import com.learning.platform.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceUnitTest {
    
    @Mock
    private CourseRepository courseRepository;
    
    @Mock
    private CategoryService categoryService;
    
    @InjectMocks
    private CourseService courseService;
    
    @Test
    void testGetCourseByIdFound() {
        // Given
        Long courseId = 1L;
        Course mockCourse = Course.builder()
                .id(courseId)
                .title("Mock Course")
                .build();
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        
        // When
        Course result = courseService.getCourseById(courseId);
        
        // Then
        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals("Mock Course", result.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
    }
    
    @Test
    void testGetCourseByIdNotFound() {
        // Given
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            courseService.getCourseById(courseId);
        });
        verify(courseRepository, times(1)).findById(courseId);
    }
    
    @Test
    void testCreateCourse() {
        // Given
        Course courseToSave = Course.builder()
                .title("New Course")
                .description("Description")
                .build();
        
        Course savedCourse = Course.builder()
                .id(1L)
                .title("New Course")
                .description("Description")
                .build();
        
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        
        // When
        Course result = courseService.createCourse(courseToSave);
        
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseRepository, times(1)).save(courseToSave);
    }
    
    @Test
    void testDeleteCourse() {
        // Given
        Long courseId = 1L;
        Course mockCourse = Course.builder()
                .id(courseId)
                .title("Course to delete")
                .build();
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        doNothing().when(courseRepository).delete(mockCourse);
        
        // When
        courseService.deleteCourse(courseId);
        
        // Then
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).delete(mockCourse);
    }
}