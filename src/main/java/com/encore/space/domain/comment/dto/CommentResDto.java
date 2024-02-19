package com.encore.space.domain.comment.dto;

import com.encore.space.domain.comment.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResDto {
    private String nickname;
    private String contents;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static CommentResDto toDto(Comment comment) {
        CommentResDto commentResDto = new CommentResDto();
        commentResDto.setNickname(comment.getMember().getNickname());
        commentResDto.setContents(comment.getContent());
        commentResDto.setCreatedTime(comment.getCreatedTime());
        commentResDto.setUpdatedTime(comment.getUpdatedTime());
        return commentResDto;
    }
}
