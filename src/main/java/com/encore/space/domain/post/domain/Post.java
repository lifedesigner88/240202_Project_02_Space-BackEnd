package com.encore.space.domain.post.domain;


import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.space.domain.Space;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @Column(length = 1500, nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
//    게시글 조회수
//    private Long viewCount;

    //게시글 공개여부
    @Enumerated(EnumType.STRING)
    private Status status;

    //조회수  --> DB?
    private Long viewCount;

    //좋아요 수
    private int likes;


    @ManyToOne   //  테스트를 위해 임시 추가(세종).
    @JoinColumn(nullable = false)
    private Space space;

//
//    //첨부파일
//    @OneToMany(mappedBy = "post")
//    private List<AttachFile> attachFileList;
//    //댓글
////    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE} )     //remove 쓸건지
//    private List<Comment> comment = new ArrayList<>();

//    public void updatePost(String title, String contents, LocalDateTime updatedTime){
//        this.title=title;
//        this.contents=contents;
//        this.updatedTime= updatedTime;
//    }
}


