package com.encore.space.domain.file.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AttachFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //게시판 글 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;
    @Column
    private String attachFileName;
    @Column
    private String attachFilePath;
    @Column
    private String fileType;
    @Column
    private Long fileSize;
    @Builder.Default
    private String delYN = "N";

    private String thumbnail;

    @Builder.Default
    private String thumbnailYn="N";

    public void delete(){
        this.delYN="Y";
    }
}
