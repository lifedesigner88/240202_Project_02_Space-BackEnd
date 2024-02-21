package com.encore.space.domain.post.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.space.domain.Space;
import jakarta.persistence.*;
import lombok.*;

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
    @Setter
    private String thumbnail;
    @Column
    @Builder.Default
    private String delYN="N";
    //첨부파일
    @OneToMany(mappedBy = "post")
    private List<AttachFile> attachFiles;

    //댓글
    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

    @ManyToOne
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

    public void deleteThumbnail(){
        this.thumbnail=null;
    }
}


