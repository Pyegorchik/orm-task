package com.learning.platform.config;

import com.learning.platform.model.*;
import com.learning.platform.model.Module;
import com.learning.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev") // Запускается только в dev профиле
public class DataSeeder implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedData();
        }
    }
    
    private void seedData() {
        // Create users
        User admin = User.builder()
                .username("admin")
                .email("admin@learning.com")
                .password("admin123")
                .firstName("Admin")
                .lastName("User")
                .role(User.UserRole.ADMIN)
                .build();
        
        User instructor = User.builder()
                .username("instructor")
                .email("instructor@learning.com")
                .password("instructor123")
                .firstName("John")
                .lastName("Smith")
                .role(User.UserRole.INSTRUCTOR)
                .build();
        
        User student1 = User.builder()
                .username("student1")
                .email("student1@learning.com")
                .password("student123")
                .firstName("Alice")
                .lastName("Johnson")
                .role(User.UserRole.STUDENT)
                .build();
        
        User student2 = User.builder()
                .username("student2")
                .email("student2@learning.com")
                .password("student123")
                .firstName("Bob")
                .lastName("Williams")
                .role(User.UserRole.STUDENT)
                .build();
        
        userRepository.saveAll(List.of(admin, instructor, student1, student2));
        
        // Create categories
        Category programming = Category.builder()
                .name("Programming")
                .description("Learn programming languages and frameworks")
                .build();
        
        Category design = Category.builder()
                .name("Design")
                .description("Graphic and web design courses")
                .build();
        
        Category business = Category.builder()
                .name("Business")
                .description("Business and management courses")
                .build();
        
        categoryRepository.saveAll(List.of(programming, design, business));
        
        // Create courses
        Course javaCourse = Course.builder()
                .title("Java Programming Masterclass")
                .description("Learn Java from beginner to expert level")
                .duration(60)
                .level(Course.CourseLevel.BEGINNER)
                .price(129.99)
                .startDate(LocalDate.now().plusDays(7))
                .instructor(instructor)
                .category(programming)
                .isPublished(true)
                .build();
        
        Course springCourse = Course.builder()
                .title("Spring Boot & Spring Cloud")
                .description("Modern backend development with Spring")
                .duration(45)
                .level(Course.CourseLevel.INTERMEDIATE)
                .price(149.99)
                .startDate(LocalDate.now().plusDays(14))
                .instructor(instructor)
                .category(programming)
                .isPublished(true)
                .build();
        
        courseRepository.saveAll(List.of(javaCourse, springCourse));
        
        // Create modules for Java course
        Module javaBasics = Module.builder()
                .title("Java Basics")
                .description("Fundamental Java concepts")
                .orderIndex(1)
                .course(javaCourse)
                .build();
        
        Module oop = Module.builder()
                .title("Object-Oriented Programming")
                .description("OOP principles in Java")
                .orderIndex(2)
                .course(javaCourse)
                .build();
        
        moduleRepository.saveAll(List.of(javaBasics, oop));
        
        // Create lessons
        Lesson lesson1 = Lesson.builder()
                .title("Introduction to Java")
                .content("Java is a high-level, class-based, object-oriented programming language...")
                .videoUrl("https://example.com/videos/java-intro.mp4")
                .durationMinutes(45)
                .module(javaBasics)
                .build();
        
        Lesson lesson2 = Lesson.builder()
                .title("Variables and Data Types")
                .content("Learn about primitive types, reference types...")
                .videoUrl("https://example.com/videos/java-variables.mp4")
                .durationMinutes(60)
                .module(javaBasics)
                .build();
        
        lessonRepository.saveAll(List.of(lesson1, lesson2));
        
        // Create assignments
        Assignment assignment1 = Assignment.builder()
                .title("First Java Program")
                .description("Write a simple 'Hello World' program")
                .maxScore(100)
                .lesson(lesson1)
                .build();
        
        assignmentRepository.save(assignment1);
        
        // Create quiz
        Quiz quiz = Quiz.builder()
                .title("Java Basics Quiz")
                .description("Test your understanding of Java fundamentals")
                .timeLimitMinutes(30)
                .passingScore(70)
                .module(javaBasics)
                .build();
        
        quizRepository.save(quiz);
        
        // Create questions for quiz
        Question question1 = Question.builder()
                .text("What is the main purpose of the JVM?")
                .type(Question.QuestionType.SINGLE_CHOICE)
                .points(10)
                .quiz(quiz)
                .build();
        
        Question question2 = Question.builder()
                .text("Which of the following are primitive data types in Java?")
                .type(Question.QuestionType.MULTIPLE_CHOICE)
                .points(20)
                .quiz(quiz)
                .build();
        
        questionRepository.saveAll(List.of(question1, question2));
        
        // Create answer options
        AnswerOption option1a = AnswerOption.builder()
                .text("To compile Java source code")
                .isCorrect(false)
                .question(question1)
                .build();
        
        AnswerOption option1b = AnswerOption.builder()
                .text("To execute Java bytecode")
                .isCorrect(true)
                .question(question1)
                .build();
        
        AnswerOption option2a = AnswerOption.builder()
                .text("int")
                .isCorrect(true)
                .question(question2)
                .build();
        
        AnswerOption option2b = AnswerOption.builder()
                .text("String")
                .isCorrect(false)
                .question(question2)
                .build();
        
        AnswerOption option2c = AnswerOption.builder()
                .text("boolean")
                .isCorrect(true)
                .question(question2)
                .build();
        
        answerOptionRepository.saveAll(List.of(option1a, option1b, option2a, option2b, option2c));
        
        System.out.println("Demo data seeded successfully!");
    }
}