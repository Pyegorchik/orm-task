package com.learning.platform.service;

import com.learning.platform.model.Lesson;
import com.learning.platform.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;
    
    public Lesson createLesson(Long moduleId, Lesson lesson) {
        lesson.setModule(moduleService.getModuleById(moduleId));
        return lessonRepository.save(lesson);
    }
    
    @Transactional(readOnly = true)
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public Lesson getLessonWithAssignments(Long id) {
        return lessonRepository.findByIdWithAssignments(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByModule(Long moduleId) {
        return lessonRepository.findByModuleId(moduleId);
    }
    
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }
    
    public Lesson updateLesson(Long id, Lesson lessonDetails) {
        Lesson lesson = getLessonById(id);
        
        lesson.setTitle(lessonDetails.getTitle());
        lesson.setContent(lessonDetails.getContent());
        lesson.setVideoUrl(lessonDetails.getVideoUrl());
        lesson.setDurationMinutes(lessonDetails.getDurationMinutes());
        
        return lessonRepository.save(lesson);
    }
    
    public void deleteLesson(Long id) {
        Lesson lesson = getLessonById(id);
        lessonRepository.delete(lesson);
    }
}