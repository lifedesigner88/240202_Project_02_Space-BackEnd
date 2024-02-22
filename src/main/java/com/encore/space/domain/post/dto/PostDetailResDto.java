package com.encore.space.domain.post.dto;

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
    private String postStatus;
    private Long postHearts;
    private int commentCounts;
    private List<String> attachFiles;
    private String spaceName;
    private String spaceType;
}
