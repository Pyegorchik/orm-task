package com.learning.platform.util;

import com.learning.platform.model.*;
import java.util.ArrayList;
import java.util.UUID;

public class TestUtils {
    
    public static String generateUniqueName(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static User createTestUserWithCollections() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = User.builder()
                .username("user_" + uniqueSuffix)
                .email("user_" + uniqueSuffix + "@test.com")
                .firstName("Test")
                .lastName("User")
                .role(User.UserRole.STUDENT)
                .build();
        user.setPassword("password123");
        
        // Инициализируем все коллекции
        if (user.getEnrollments() == null) user.setEnrollments(new ArrayList<>());
        if (user.getSubmissions() == null) user.setSubmissions(new ArrayList<>());
        if (user.getQuizSubmissions() == null) user.setQuizSubmissions(new ArrayList<>());
        if (user.getReviews() == null) user.setReviews(new ArrayList<>());
        if (user.getCoursesTaught() == null) user.setCoursesTaught(new ArrayList<>());
        
        return user;
    }
    
    public static Category createTestCategory() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return Category.builder()
                .name("Category_" + uniqueSuffix)
                .description("Test category")
                .build();
    }
    
    public static Course createTestCourse(User instructor, Category category) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Course course = Course.builder()
                .title("Course_" + uniqueSuffix)
                .description("Test course")
                .instructor(instructor)
                .category(category)
                .build();
        
        if (course.getEnrollments() == null) course.setEnrollments(new ArrayList<>());
        if (course.getModules() == null) course.setModules(new ArrayList<>());
        if (course.getReviews() == null) course.setReviews(new ArrayList<>());
        
        return course;
    }
    
    public static Quiz createTestQuiz() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Quiz quiz = Quiz.builder()
                .title("Quiz_" + uniqueSuffix)
                .description("Test quiz")
                .build();
        
        if (quiz.getQuestions() == null) quiz.setQuestions(new ArrayList<>());
        if (quiz.getQuizSubmissions() == null) quiz.setQuizSubmissions(new ArrayList<>());
        
        return quiz;
    }
}