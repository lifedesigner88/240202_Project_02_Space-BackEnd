package com.encore.space.domain.post.repository;

import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.space.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findBySpace(Space space);
}
