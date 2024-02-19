package com.encore.space.domain.hearts;

import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.hearts.domain.Heart;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HeartRepository extends JpaRepository<Heart,Long> {

    @Query("select h from Heart h where h.member = :member and h.post = :post")
    Optional<Heart> findByMemberAndPost(@Param("member") Member member, @Param("post") Post post);

    @Query("select h from Heart h where h.member = :member and h.comment = :comment")
    Optional<Heart> findByMemberAndComment(@Param("member") Member member, @Param("comment") Comment comment);

    List<Heart> findByCommentId(Long id);
    List<Heart> findByPostId(Long id);

}
