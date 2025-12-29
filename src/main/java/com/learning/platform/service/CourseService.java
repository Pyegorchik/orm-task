package com.learning.platform.service;

import com.learning.platform.model.Course;
import com.learning.platform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final CategoryService categoryService;
    
    public Course createCourse(Course course) {
        // Здесь можно добавить бизнес-логику
        return courseRepository.save(course);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public Course getCourseByIdWithCategory(Long id) {
        return courseRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
    
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        
        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        course.setImageUrl(courseDetails.getImageUrl());
        course.setDuration(courseDetails.getDuration());
        course.setLevel(courseDetails.getLevel());
        course.setPrice(courseDetails.getPrice());
        course.setStartDate(courseDetails.getStartDate());
        course.setIsPublished(courseDetails.getIsPublished());
        
        if (courseDetails.getCategory() != null) {
            course.setCategory(courseDetails.getCategory());
        }
        
        if (courseDetails.getInstructor() != null) {
            course.setInstructor(courseDetails.getInstructor());
        }
        
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }
    
    @Transactional(readOnly = true)
    public List<Course> getPublishedCourses() {
        return courseRepository.findByIsPublishedTrue();
    }
}