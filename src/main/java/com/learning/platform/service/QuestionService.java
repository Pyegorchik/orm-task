package com.learning.platform.service;

import com.learning.platform.model.*;
import com.learning.platform.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    
    public int calculateQuestionScore(Question question, List<Long> selectedOptionIds) {
        List<AnswerOption> correctOptions = question.getOptions().stream()
                .filter(AnswerOption::getIsCorrect)
                .toList();
        
        if (question.getType() == Question.QuestionType.SINGLE_CHOICE) {
            // Для одиночного выбора: правильный ответ, если выбран один правильный вариант
            if (selectedOptionIds.size() == 1) {
                Long selectedId = selectedOptionIds.get(0);
                return correctOptions.stream()
                        .anyMatch(option -> option.getId().equals(selectedId)) ? question.getPoints() : 0;
            }
            return 0;
        } else {
            // Для множественного выбора: частичные баллы
            long correctSelected = selectedOptionIds.stream()
                    .filter(selectedId -> correctOptions.stream()
                            .anyMatch(option -> option.getId().equals(selectedId)))
                    .count();
            
            long incorrectSelected = selectedOptionIds.size() - correctSelected;
            
            // Формула: (правильные - неправильные) * баллы
            double score = Math.max(0, correctSelected - incorrectSelected) * question.getPoints();
            return (int) score;
        }
    }
    
    @Transactional(readOnly = true)
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    }
    
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }
    
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}