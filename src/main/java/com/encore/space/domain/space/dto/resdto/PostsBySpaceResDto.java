package com.encore.space.domain.space.dto.resdto;


import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.space.domain.Space;
import lombok.Getter;

@Getter
public class PostsBySpaceResDto {

    private final String spaceType;

    private final Long AuthorId;
    private final String AuthorEmail;
    private final String AuthorName;
    private final String AuthorNickName;

    private final Long postId;
    private final String title;
    private final String contents;
    private final Long viewCount;
    private final int likes;


    public PostsBySpaceResDto (Post post) {

        Member member = post.getMember();
        Space space = post.getSpace();

        this.spaceType = space.getSpaceType().toString();

        this.AuthorId = member.getId();
        this.AuthorEmail = member.getEmail();
        this.AuthorName = member.getName();
        this.AuthorNickName = member.getNickname();

        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.viewCount = post.getViewCount();
        this.likes = post.getLikes();

    }


}
