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
        private Long postId;
        private String title;
        private String nickname;
        private String thumbnail;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private PostStatus postStatus;
        private Long spaceId;
}
