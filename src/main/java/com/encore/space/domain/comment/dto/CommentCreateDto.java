package com.encore.space.domain.comment.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private Long memberId;
    private String contents;
    private Long parentId;

}
