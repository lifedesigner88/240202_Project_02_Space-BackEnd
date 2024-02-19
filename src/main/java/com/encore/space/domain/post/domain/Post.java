package com.encore.space.domain.post.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.space.domain.Space;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
    //게시글 공개여부
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus postStatus = PostStatus.OPEN;
    @Column
    @Builder.Default
    private String delYN="N";
    //첨부파일
    @OneToMany(mappedBy = "post")
    private List<AttachFile> attachFiles;
    //댓글
    @OneToMany(mappedBy = "post")
    private List<Comment> comment;
    @ManyToOne   //  테스트를 위해 임시 추가(세종).
    @JoinColumn(nullable = false)
    private Space space;


    public void updatePost(String title, String contents, PostStatus postStatus){
        this.title=title;
        this.contents=contents;
        this.postStatus = postStatus;
    }

    public void deletePost(){
        this.delYN="Y";
    }
  
}


