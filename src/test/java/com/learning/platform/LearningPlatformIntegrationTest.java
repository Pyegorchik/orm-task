package com.learning.platform;

import com.learning.platform.model.*;
import com.learning.platform.model.Module;
import com.learning.platform.repository.*;
import com.learning.platform.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LearningPlatformIntegrationTest {
    
//     @Autowired
//     private UserRepository userRepository;
    
//     @Autowired
//     private CourseRepository courseRepository;
    
//     @Autowired
//     private CategoryRepository categoryRepository;
    
//     @Autowired
//     private EnrollmentRepository enrollmentRepository;
    
//     @Autowired
//     private ModuleRepository moduleRepository;
    
//     @Autowired
//     private LessonRepository lessonRepository;
    
//     @Autowired
//     private AssignmentRepository assignmentRepository;
    
//     @Autowired
//     private SubmissionRepository submissionRepository;
    
//     @Autowired
//     private QuizRepository quizRepository;
    
//     @Autowired
//     private QuestionRepository questionRepository;
    
//     @Autowired
//     private AnswerOptionRepository answerOptionRepository;
    
//     @Autowired
//     private CourseService courseService;
    
//     @Autowired
//     private QuizService quizService;
    
//     @Test
//     void contextLoads() {
//         assertThat(userRepository).isNotNull();
//         assertThat(courseRepository).isNotNull();
//     }
    
//     @Test
//     void testCreateUserWithProfile() {
//         // Create user
//         User user = User.builder()
//                 .name("John Doe")
//                 .email("john@example.com")
//                 .role(User.UserRole.STUDENT)
//                 .phone("+1234567890")
//                 .build();
        
//         // Create profile
//         Profile profile = Profile.builder()
//                 .user(user)
//                 .bio("Passionate learner")
//                 .avatarUrl("https://example.com/avatar.jpg")
//                 .build();
        
//         user.setProfile(profile);
        
//         User savedUser = userRepository.save(user);
        
//         assertThat(savedUser.getId()).isNotNull();
//         assertThat(savedUser.getProfile()).isNotNull();
//         assertThat(savedUser.getProfile().getBio()).isEqualTo("Passionate learner");
//     }
    
//     @Test
//     void testLazyLoadingException() {
//         // Create and save user
//         User user = User.builder()
//                 .name("Jane Smith")
//                 .email("jane@example.com")
//                 .role(User.UserRole.STUDENT)
//                 .build();
        
//         userRepository.save(user);
//         userRepository.flush();
        
//         // Clear persistence context
//         userRepository.findAll();
        
//         // Try to access lazy-loaded collection outside transaction
//         // This should demonstrate the LazyInitializationException pattern
//         User foundUser = userRepository.findById(user.getId()).get();
        
//         // This will work inside @Transactional test
//         assertDoesNotThrow(() -> foundUser.getEnrollments().size());
//     }
    
//     @Test
//     void testCreateCourseWithModulesAndLessons() {
//         // Create teacher
//         User teacher = User.builder()
//                 .name("Prof. Smith")
//                 .email("prof.smith@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build();
//         userRepository.save(teacher);
        
//         // Create category
//         Category category = Category.builder()
//                 .name("Programming")
//                 .description("Programming courses")
//                 .build();
//         categoryRepository.save(category);
        
//         // Create course
//         Course course = Course.builder()
//                 .title("Java Masterclass")
//                 .description("Complete Java programming course")
//                 .duration(12)
//                 .startDate(LocalDate.now())
//                 .level(Course.CourseLevel.INTERMEDIATE)
//                 .instructor(teacher)
//                 .category(category)
//                 .build();
        
//         Course savedCourse = courseRepository.save(course);
        
//         // Add module
//         Module module = Module.builder()
//                 .title("Introduction to Java")
//                 .description("Learn Java basics")
//                 .orderIndex(1)
//                 .course(savedCourse)
//                 .build();
        
//         Module savedModule = moduleRepository.save(module);
        
//         // Add lesson
//         Lesson lesson = Lesson.builder()
//                 .title("Variables and Data Types")
//                 .content("Learn about Java variables")
//                 .duration(30)
//                 .orderIndex(1)
//                 .module(savedModule)
//                 .build();
        
//         Lesson savedLesson = lessonRepository.save(lesson);
        
//         assertThat(savedCourse.getId()).isNotNull();
//         assertThat(savedModule.getCourse().getId()).isEqualTo(savedCourse.getId());
//         assertThat(savedLesson.getModule().getId()).isEqualTo(savedModule.getId());
//     }
    
//     @Test
//     void testEnrollStudentInCourse() {
//         // Create student
//         User student = User.builder()
//                 .name("Alice Johnson")
//                 .email("alice@example.com")
//                 .role(User.UserRole.STUDENT)
//                 .build();
//         userRepository.save(student);
        
//         // Create teacher and course
//         User teacher = User.builder()
//                 .name("Prof. Brown")
//                 .email("prof.brown@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build();
//         userRepository.save(teacher);
        
//         Course course = Course.builder()
//                 .title("Python Programming")
//                 .description("Learn Python")
//                 .instructor(teacher)
//                 .build();
//         courseRepository.save(course);
        
//         // Enroll student
//         Enrollment enrollment = Enrollment.builder()
//                 .student(student)
//                 .course(course)
//                 .status(Enrollment.EnrollmentStatus.ACTIVE)
//                 .enrollDate(LocalDateTime.now())
//                 .progress(0)
//                 .build();
        
//         Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
//         assertThat(savedEnrollment.getId()).isNotNull();
//         assertThat(savedEnrollment.getUser().getId()).isEqualTo(student.getId());
//         assertThat(savedEnrollment.getCourse().getId()).isEqualTo(course.getId());
//         assertThat(savedEnrollment.getStatus()).isEqualTo(Enrollment.EnrollmentStatus.ACTIVE);
//     }
    
//     @Test
//     void testCreateAssignmentAndSubmission() {
//         // Setup: Create complete hierarchy
//         User teacher = userRepository.save(User.builder()
//                 .name("Teacher")
//                 .email("teacher@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build());
        
//         User student = userRepository.save(User.builder()
//                 .name("Student")
//                 .email("student@example.com")
//                 .role(User.UserRole.STUDENT)
//                 .build());
        
//         Course course = courseRepository.save(Course.builder()
//                 .title("Test Course")
//                 .description("Course for testing")
//                 .instructor(teacher)
//                 .build());
        
//         Module module = moduleRepository.save(Module.builder()
//                 .title("Module 1")
//                 .orderIndex(1)
//                 .course(course)
//                 .build());
        
//         Lesson lesson = lessonRepository.save(Lesson.builder()
//                 .title("Lesson 1")
//                 .content("Content")
//                 .orderIndex(1)
//                 .module(module)
//                 .build());
        
//         // Create assignment
//         Assignment assignment = Assignment.builder()
//                 .title("Homework 1")
//                 .description("Complete the exercises")
//                 .dueDate(LocalDateTime.now().plusDays(7))
//                 .maxScore(100)
//                 .type(Assignment.AssignmentType.ESSAY)
//                 .lesson(lesson)
//                 .build();
        
//         Assignment savedAssignment = assignmentRepository.save(assignment);
        
//         // Student submits assignment
//         Submission submission = Submission.builder()
//                 .assignment(savedAssignment)
//                 .student(student)
//                 .content("My solution to the homework")
//                 .submittedAt(LocalDateTime.now())
//                 .status(Submission.SubmissionStatus.SUBMITTED)
//                 .build();
        
//         Submission savedSubmission = submissionRepository.save(submission);
        
//         assertThat(savedSubmission.getId()).isNotNull();
//         assertThat(savedSubmission.getAssignment().getId()).isEqualTo(savedAssignment.getId());
//         assertThat(savedSubmission.getStudent().getId()).isEqualTo(student.getId());
//     }
    
//     @Test
//     void testCreateQuizWithQuestionsAndAnswers() {
//         // Create module
//         User teacher = userRepository.save(User.builder()
//                 .name("Teacher")
//                 .email("quiz.teacher@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build());
        
//         Course course = courseRepository.save(Course.builder()
//                 .title("Quiz Course")
//                 .teacher(teacher)
//                 .build());
        
//         Module module = moduleRepository.save(Module.builder()
//                 .title("Module with Quiz")
//                 .orderIndex(1)
//                 .course(course)
//                 .build());
        
//         // Create quiz
//         Quiz quiz = Quiz.builder()
//                 .title("Java Basics Quiz")
//                 .description("Test your Java knowledge")
//                 .timeLimit(30)
//                 .passingScore(70)
//                 .module(module)
//                 .build();
        
//         Quiz savedQuiz = quizRepository.save(quiz);
        
//         // Create question
//         Question question = Question.builder()
//                 .text("What is the size of int in Java?")
//                 .type(Question.QuestionType.SINGLE_CHOICE)
//                 .points(10)
//                 .orderIndex(1)
//                 .quiz(savedQuiz)
//                 .build();
        
//         Question savedQuestion = questionRepository.save(question);
        
//         // Create answer options
//         AnswerOption option1 = answerOptionRepository.save(AnswerOption.builder()
//                 .text("16 bits")
//                 .isCorrect(false)
//                 .orderIndex(1)
//                 .question(savedQuestion)
//                 .build());
        
//         AnswerOption option2 = answerOptionRepository.save(AnswerOption.builder()
//                 .text("32 bits")
//                 .isCorrect(true)
//                 .orderIndex(2)
//                 .question(savedQuestion)
//                 .build());
        
//         assertThat(savedQuiz.getId()).isNotNull();
//         assertThat(savedQuestion.getQuiz().getId()).isEqualTo(savedQuiz.getId());
//         assertThat(option2.getIsCorrect()).isTrue();
//     }
    
//     @Test
//     void testManyToManyRelationshipWithTags() {
//         User teacher = userRepository.save(User.builder()
//                 .name("Teacher")
//                 .email("tag.teacher@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build());
        
//         Course course = courseRepository.save(Course.builder()
//                 .title("Tagged Course")
//                 .teacher(teacher)
//                 .build());
        
//         Tag tag1 = new Tag();
//         tag1.setName("Java");
        
//         Tag tag2 = new Tag();
//         tag2.setName("Beginner");
        
//         course.getTags().add(tag1);
//         course.getTags().add(tag2);
//         tag1.getCourses().add(course);
//         tag2.getCourses().add(course);
        
//         courseRepository.save(course);
        
//         Course foundCourse = courseRepository.findById(course.getId()).get();
//         assertThat(foundCourse.getTags()).hasSize(2);
//     }
    
//     @Test
//     void testCascadeOperations() {
//         // Create course with cascading entities
//         User teacher = userRepository.save(User.builder()
//                 .name("Cascade Teacher")
//                 .email("cascade@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build());
        
//         Course course = Course.builder()
//                 .title("Cascade Test Course")
//                 .teacher(teacher)
//                 .build();
        
//         Module module = Module.builder()
//                 .title("Module 1")
//                 .orderIndex(1)
//                 .build();
        
//         course.addModule(module);
        
//         Lesson lesson = Lesson.builder()
//                 .title("Lesson 1")
//                 .content("Content")
//                 .orderIndex(1)
//                 .build();
        
//         module.addLesson(lesson);
        
//         // Save only course - cascade should save module and lesson
//         Course savedCourse = courseRepository.save(course);
        
//         assertThat(savedCourse.getModules()).hasSize(1);
//         Module savedModule = savedCourse.getModules().iterator().next();
//         assertThat(savedModule.getLessons()).hasSize(1);
//     }
    
//     @Test
//     void testOrphanRemoval() {
//         User teacher = userRepository.save(User.builder()
//                 .name("Teacher")
//                 .email("orphan.teacher@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build());
        
//         Course course = Course.builder()
//                 .title("Orphan Test Course")
//                 .teacher(teacher)
//                 .build();
        
//         Module module = Module.builder()
//                 .title("Module to Remove")
//                 .orderIndex(1)
//                 .build();
        
//         course.addModule(module);
//         courseRepository.save(course);
        
//         // Remove module from collection
//         course.getModules().clear();
//         courseRepository.save(course);
        
//         // Module should be deleted due to orphanRemoval=true
//         assertThat(moduleRepository.findById(module.getId())).isEmpty();
//     }
    
//     @Test
//     void testQueryMethods() {
//         // Create test data
//         User teacher = userRepository.save(User.builder()
//                 .name("Query Teacher")
//                 .email("query.teacher@example.com")
//                 .role(User.UserRole.TEACHER)
//                 .build());
        
//         Category category = categoryRepository.save(Category.builder()
//                 .name("Test Category")
//                 .build());
        
//         courseRepository.save(Course.builder()
//                 .title("Course 1")
//                 .teacher(teacher)
//                 .category(category)
//                 .build());
        
//         courseRepository.save(Course.builder()
//                 .title("Course 2")
//                 .teacher(teacher)
//                 .category(category)
//                 .build());
        
//         // Test custom query methods
//         List<Course> teacherCourses = courseRepository.findByTeacherId(teacher.getId());
//         assertThat(teacherCourses).hasSize(2);
        
//         List<Course> categoryCourses = courseRepository.findByCategoryId(category.getId());
//         assertThat(categoryCourses).hasSize(2);
        
//         List<User> teachers = userRepository.findByRole(User.UserRole.TEACHER);
//         assertThat(teachers).hasSizeGreaterThanOrEqualTo(1);
//     }
}
