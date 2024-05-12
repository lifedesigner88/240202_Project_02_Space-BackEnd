package com.encore.space.domain.comment.dto;

import com.encore.space.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentChildrenListDto {
    private String nickname;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


}
