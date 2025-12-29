package com.learning.platform.controller;

import com.learning.platform.model.*;
import com.learning.platform.model.Module;
import com.learning.platform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    private final ModuleService moduleService;
    private final LessonService lessonService;
    private final AssignmentService assignmentService;
    private final EnrollmentService enrollmentService;
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    
    // @GetMapping("/{id}/with-modules")
    // public ResponseEntity<Course> getCourseWithModules(@PathVariable Long id) {
    //     return ResponseEntity.ok(courseService.getCourseByIdWithCategory(id));
    // }
    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(categoryId));
    }
    
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(course));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id, 
            @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<Module>> getModulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(moduleService.getModulesByCourse(courseId));
    }
    
    @PostMapping("/{courseId}/modules")
    public ResponseEntity<Module> addModuleToCourse(
            @PathVariable Long courseId,
            @RequestBody Module module) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(moduleService.createModule(courseId, module));
    }
    
    @GetMapping("/modules/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleService.getModuleById(moduleId));
    }
    
    @PutMapping("/modules/{moduleId}")
    public ResponseEntity<Module> updateModule(
            @PathVariable Long moduleId,
            @RequestBody Module module) {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, module));
    }
    
    @DeleteMapping("/modules/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<Lesson>> getLessonsByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId));
    }
    
    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Lesson> addLessonToModule(
            @PathVariable Long moduleId,
            @RequestBody Lesson lesson) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.createLesson(moduleId, lesson));
    }
    
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getLessonById(lessonId));
    }
    
    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody Lesson lesson) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, lesson));
    }
    
    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/lessons/{lessonId}/assignments")
    public ResponseEntity<List<Assignment>> getAssignmentsByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByLesson(lessonId));
    }
    
    @PostMapping("/lessons/{lessonId}/assignments")
    public ResponseEntity<Assignment> addAssignmentToLesson(
            @PathVariable Long lessonId,
            @RequestBody Assignment assignment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.createAssignment(lessonId, assignment));
    }
    
    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(assignmentId));
    }
    
    @PutMapping("/assignments/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long assignmentId,
            @RequestBody Assignment assignment) {
        return ResponseEntity.ok(assignmentService.updateAssignment(assignmentId, assignment));
    }
    
    @DeleteMapping("/assignments/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Enrollment> enrollStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollUser(studentId, courseId));
    }
    
    @GetMapping("/{courseId}/enrollments")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId));
    }
    
    @GetMapping("/{courseId}/enrollments/active")
    public ResponseEntity<List<Enrollment>> getActiveEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId).stream()
                .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ACTIVE)
                .toList());
    }
    
    @PutMapping("/enrollments/{enrollmentId}/complete")
    public ResponseEntity<Enrollment> completeEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.completeEnrollment(enrollmentId));
    }
    
    @PutMapping("/enrollments/{enrollmentId}/cancel")
    public ResponseEntity<Enrollment> cancelEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.cancelEnrollment(enrollmentId));
    }
}