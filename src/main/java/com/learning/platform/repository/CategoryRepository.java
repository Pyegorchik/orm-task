package com.learning.platform.repository;

import com.learning.platform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.courses")
    List<Category> findAllWithCourses();
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.courses WHERE c.id = :id")
    Optional<Category> findByIdWithCourses(Long id);
}

