package com.encore.space.domain.comment.repository;

import com.encore.space.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL")
    List<Comment> findAllParentCommentsByPostId(@Param("postId") Long postId);
    List<Comment> findAllByPostIdAndParentCommentId(Long postId, Long parentCommentId);
    List<Comment> findAllByPostId(Long postId);
}
