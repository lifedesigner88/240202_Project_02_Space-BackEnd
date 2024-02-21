package com.encore.space.domain.post.dto;

import com.encore.space.domain.post.domain.PostStatus;
import com.encore.space.domain.space.domain.Space;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDetailResDto {
    private String title;
    private String contents;
    private String nickname;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private PostStatus postStatus;
    private Long postHearts;
    private int commentCounts;
    private String thumbnail;
    private List<String> attachFiles;
    private Space space;
}
