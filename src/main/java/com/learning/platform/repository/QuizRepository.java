package com.learning.platform.repository;

import com.learning.platform.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByModuleId(Long moduleId);
    
    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questions WHERE q.id = :id")
    Optional<Quiz> findByIdWithQuestions(@Param("id") Long id);
    
    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questions LEFT JOIN FETCH q.module WHERE q.id = :id")
    Optional<Quiz> findByIdWithQuestionsAndModule(@Param("id") Long id);
    
    @Query("SELECT q FROM Quiz q WHERE q.module.course.id = :courseId")
    List<Quiz> findByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.module.id = :moduleId")
    Long countByModuleId(@Param("moduleId") Long moduleId);

     @Query("SELECT DISTINCT q FROM Quiz q " +
           "LEFT JOIN FETCH q.questions qq " +
           "LEFT JOIN FETCH qq.options " +
           "WHERE q.id = :id")
    Optional<Quiz> findByIdWithQuestionsAndOptions(@Param("id") Long id);
}