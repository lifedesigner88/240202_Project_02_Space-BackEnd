package com.encore.space.domain.post.dto;

import com.encore.space.domain.post.domain.PostStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDetailResDto {
    private Long id;
    private String title;
    private String contents;
    private String nickname;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private PostStatus postStatus;
    private Long postHearts;
    private String thumbnail;
    private List<String> attachFiles;
}
