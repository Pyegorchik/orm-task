package com.learning.platform.repository;

import com.learning.platform.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    // List<AnswerOption> findByQuestionIdOrderByOrderIndex(Long questionId);
    List<AnswerOption> findByQuestionIdAndIsCorrect(Long questionId, Boolean isCorrect);

    List<AnswerOption> findByQuestionId(Long questionId);
    
    @Query("SELECT ao FROM AnswerOption ao WHERE ao.question.id = :questionId AND ao.isCorrect = true")
    List<AnswerOption> findCorrectOptionsByQuestionId(@Param("questionId") Long questionId);
    
    @Query("SELECT COUNT(ao) FROM AnswerOption ao WHERE ao.question.id = :questionId AND ao.isCorrect = true")
    Long countCorrectOptionsByQuestionId(@Param("questionId") Long questionId);
}
