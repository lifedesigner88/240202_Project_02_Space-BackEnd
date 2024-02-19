package com.encore.space.domain.post.dto;

import com.encore.space.domain.post.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PostListDto {
        private Long id;
        private String title;
        private String nickname;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
        private PostStatus postStatus;
}

