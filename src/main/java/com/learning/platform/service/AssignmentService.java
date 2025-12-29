package com.learning.platform.service;

import com.learning.platform.model.Assignment;
import com.learning.platform.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {
    
    private final AssignmentRepository assignmentRepository;
    private final LessonService lessonService;
    
    public Assignment createAssignment(Long lessonId, Assignment assignment) {
        assignment.setLesson(lessonService.getLessonById(lessonId));
        return assignmentRepository.save(assignment);
    }
    
    @Transactional(readOnly = true)
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByLesson(Long lessonId) {
        return assignmentRepository.findByLessonId(lessonId);
    }
    
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }
    
    public Assignment updateAssignment(Long id, Assignment assignmentDetails) {
        Assignment assignment = getAssignmentById(id);
        
        assignment.setTitle(assignmentDetails.getTitle());
        assignment.setDescription(assignmentDetails.getDescription());
        assignment.setDueDate(assignmentDetails.getDueDate());
        assignment.setMaxScore(assignmentDetails.getMaxScore());
        
        return assignmentRepository.save(assignment);
    }
    
    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentById(id);
        assignmentRepository.delete(assignment);
    }
}