package com.learning.platform.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(name = "enroll_date")
    private LocalDateTime enrollDate;
    
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
    
    @PrePersist
    protected void onCreate() {
        enrollDate = LocalDateTime.now();
        if (status == null) {
            status = EnrollmentStatus.ACTIVE;
        }
    }
    
    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED
    }
}

