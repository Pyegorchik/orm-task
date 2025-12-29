package com.learning.platform.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class QuizSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "score")
    private Integer score;
    
    @Column(name = "max_score")
    private Integer maxScore;
    
    @Column(name = "percentage")
    private Double percentage;
    
    @Column(name = "taken_at")
    private LocalDateTime takenAt;
    
    @Enumerated(EnumType.STRING)
    private QuizStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    public enum QuizStatus {
        PENDING, PASSED, FAILED
    }
    
    @PrePersist
    protected void onCreate() {
        takenAt = LocalDateTime.now();
        if (status == null) {
            status = QuizStatus.PENDING;
        }
    }

    public Double calculatePercentage() {
        if (maxScore == null || maxScore == 0) {
            return 0.0;
        }
        return (score * 100.0) / maxScore;
    }
}