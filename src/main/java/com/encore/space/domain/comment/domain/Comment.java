package com.encore.space.domain.comment.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(nullable = false, name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Builder.Default
    private String delYN = "N";


    //대댓글           reference: https://velog.io/@ssm2053/Spring-%EB%8C%93%EA%B8%80%EC%9D%98-%EB%8B%B5%EA%B8%80-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%EB%8C%80%EB%8C%93%EA%B8%80-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84
    //부모 댓글 -> 대댓글을 list 형식으로
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parentId")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> childrenComment;

    //댓글 수정
    public void updateComment(String content) {
        this.content = content;
    }

    //댓글 삭제
    public void delete(){
        this.delYN="Y";
    }


}