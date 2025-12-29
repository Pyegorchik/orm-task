package com.learning.platform.service;

import com.learning.platform.model.*;
import com.learning.platform.repository.QuizRepository;
import com.learning.platform.repository.QuizSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {
    
    private final QuizRepository quizRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuestionService questionService;
    private final UserService userService;
    
    @Transactional(readOnly = true)
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public Quiz getQuizWithQuestions(Long id) {
        return quizRepository.findByIdWithQuestions(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Quiz> getQuizzesByModule(Long moduleId) {
        return quizRepository.findByModuleId(moduleId);
    }
    
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
    
    public Quiz updateQuiz(Long id, Quiz quizDetails) {
        Quiz quiz = getQuizById(id);
        
        quiz.setTitle(quizDetails.getTitle());
        quiz.setDescription(quizDetails.getDescription());
        quiz.setTimeLimitMinutes(quizDetails.getTimeLimitMinutes());
        quiz.setPassingScore(quizDetails.getPassingScore());
        
        if (quizDetails.getModule() != null) {
            quiz.setModule(quizDetails.getModule());
        }
        
        return quizRepository.save(quiz);
    }
    
    public void deleteQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        quizRepository.delete(quiz);
    }
    
   @Transactional
    public QuizSubmission submitQuiz(Long quizId, Long userId, Map<Long, List<Long>> userAnswers) {
        // Используем метод, который загружает вопросы с options
        Quiz quiz = quizRepository.findByIdWithQuestionsAndOptions(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        User student = userService.getUserById(userId);
        
        int totalScore = 0;
        int maxScore = quiz.getMaxScore();
        
        // Проверяем ответы на каждый вопрос
        for (Question question : quiz.getQuestions()) {
            List<Long> selectedOptions = userAnswers.get(question.getId());
            
            if (selectedOptions != null) {
                // Убедимся, что options загружены
                if (question.getOptions() == null) {
                    throw new IllegalStateException("Question options not loaded for question: " + question.getId());
                }
                
                int questionScore = questionService.calculateQuestionScore(question, selectedOptions);
                totalScore += questionScore;
            }
        }
        
        // Создаем QuizSubmission с помощью Builder
        QuizSubmission submission = QuizSubmission.builder()
                .quiz(quiz)
                .student(student)
                .score(totalScore)
                .maxScore(maxScore)
                .percentage((double) totalScore / maxScore * 100)
                .takenAt(LocalDateTime.now())
                .build();
        
        // Определяем статус прохождения
        if (quiz.getPassingScore() != null) {
            double percentage = submission.getPercentage();
            submission.setStatus(percentage >= quiz.getPassingScore() ? 
                    QuizSubmission.QuizStatus.PASSED : QuizSubmission.QuizStatus.FAILED);
        } else {
            submission.setStatus(QuizSubmission.QuizStatus.PASSED);
        }
        
        // Используем helper methods для установки связей
        quiz.addQuizSubmission(submission);
        student.addQuizSubmission(submission);
        
        return quizSubmissionRepository.save(submission);
    }
    
    @Transactional(readOnly = true)
    public List<QuizSubmission> getQuizSubmissions(Long quizId) {
        return quizSubmissionRepository.findByQuizId(quizId);
    }
    
    @Transactional(readOnly = true)
    public List<QuizSubmission> getUserQuizSubmissions(Long userId, Long quizId) {
        return quizSubmissionRepository.findByQuizIdAndStudentId(quizId, userId);
    }
    
    @Transactional(readOnly = true)
    public Double getAverageScore(Long quizId) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findByQuizId(quizId);
        if (submissions.isEmpty()) {
            return 0.0;
        }
        return submissions.stream()
                .mapToInt(QuizSubmission::getScore)
                .average()
                .orElse(0.0);
    }
    
    @Transactional(readOnly = true)
    public Double getPassRate(Long quizId) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findByQuizId(quizId);
        if (submissions.isEmpty()) {
            return 0.0;
        }
        
        long passedCount = submissions.stream()
                .filter(s -> s.getStatus() == QuizSubmission.QuizStatus.PASSED)
                .count();
        
        return (double) passedCount / submissions.size() * 100;
    }
}