package com.encore.space.domain.comment.dto;

import com.encore.space.domain.comment.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentChildrenListDto {
    private String nickname;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static CommentChildrenListDto toDto(Comment comment) {
        CommentChildrenListDto commentChildrenListDto= new CommentChildrenListDto();
        commentChildrenListDto.setNickname(comment.getMember().getNickname());
        commentChildrenListDto.setContent(comment.getContent());
        commentChildrenListDto.setCreatedTime(comment.getCreatedTime());
        commentChildrenListDto.setUpdatedTime(comment.getUpdatedTime());
        return commentChildrenListDto;
    }
}
