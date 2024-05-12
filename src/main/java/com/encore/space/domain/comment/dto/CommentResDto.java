package com.encore.space.domain.comment.dto;

import com.encore.space.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResDto {
    private String nickname;
    private String contents;
    private Long commentHearts;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
