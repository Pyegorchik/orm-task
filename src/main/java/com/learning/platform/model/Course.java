package com.learning.platform.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@EqualsAndHashCode(of = "id")
@Builder
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String imageUrl;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    private Integer duration; // in hours
    
    @Enumerated(EnumType.STRING)
    private CourseLevel level;
    
    private Double price;
    
    @Column(name = "is_published")
    private Boolean isPublished = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_tags",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    // Helper method
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getCourses().add(this);
    }
    

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Module> modules = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourseReview> reviews = new ArrayList<>();
    
    public enum CourseLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    
    // Helper methods
    public void addModule(Module module) {
        modules.add(module);
        module.setCourse(this);
    }
    
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }
    
    public void addReview(CourseReview review) {
        reviews.add(review);
        review.setCourse(this);
    }
    
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(CourseReview::getRating)
                .average()
                .orElse(0.0);
    }
    
    public Integer getEnrollmentCount() {
        return enrollments != null ? enrollments.size() : 0;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isPublished == null) {
            isPublished = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Integer getDuration() {
        return duration;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public CourseLevel getLevel() {
        return level;
    }
}