package com.encore.space.domain.space.dto.resdto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostsBySpaceResDto {

    private final String spaceType;

    private final Long AuthorId;
    private final String AuthorEmail;
    private final String AuthorName;
    private final String AuthorNickName;

    private final Long postId;
    private final String title;
    private final String contents;

}
