package com.learning.platform.repository;

import com.learning.platform.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByQuizId(Long quizId);
    List<QuizSubmission> findByStudentId(Long studentId);
    List<QuizSubmission> findByQuizIdAndStudentId(Long quizId, Long studentId);
    List<QuizSubmission> findByQuizIdAndStatus(Long quizId, QuizSubmission.QuizStatus status);
}
