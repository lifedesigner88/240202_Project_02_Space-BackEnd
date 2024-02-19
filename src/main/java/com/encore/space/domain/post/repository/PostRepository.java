package com.encore.space.domain.post.repository;

import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.space.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySpace(Space space);
}
