package com.learning.platform.service;

import com.learning.platform.model.Module;
import com.learning.platform.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModuleService {
    
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;
    
    public Module createModule(Long courseId, Module module) {
        module.setCourse(courseService.getCourseById(courseId));
        return moduleRepository.save(module);
    }
    
    @Transactional(readOnly = true)
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public Module getModuleWithLessons(Long id) {
        return moduleRepository.findByIdWithLessons(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Module> getModulesByCourse(Long courseId) {
        return moduleRepository.findByCourseIdOrderByOrderIndex(courseId);
    }
    
    @Transactional(readOnly = true)
    public List<Module> getModulesByCourseWithLessons(Long courseId) {
        return moduleRepository.findByCourseIdWithLessons(courseId);
    }
    
    public Module updateModule(Long id, Module moduleDetails) {
        Module module = getModuleById(id);
        
        module.setTitle(moduleDetails.getTitle());
        module.setDescription(moduleDetails.getDescription());
        module.setOrderIndex(moduleDetails.getOrderIndex());
        
        return moduleRepository.save(module);
    }
    
    public void deleteModule(Long id) {
        Module module = getModuleById(id);
        moduleRepository.delete(module);
    }
}