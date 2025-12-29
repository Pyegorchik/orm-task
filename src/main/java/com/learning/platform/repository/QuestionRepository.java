package com.learning.platform.repository;

import com.learning.platform.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // List<Question> findByQuizIdOrderByOrderIndex(Long quizId);
    List<Question> findByQuizId(Long quizId);
    List<Question> findByQuizIdOrderById(Long quizId);
}