package com.learning.platform.repository;

import com.learning.platform.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // List<Comment> findByDiscussionIdOrderByCreatedAt(Long discussionId);
    List<Comment> findByParentCommentId(Long parentCommentId);
}
