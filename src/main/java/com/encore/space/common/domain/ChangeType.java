package com.encore.space.common.domain;


import com.encore.space.common.config.PasswordConfig;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.dto.PostCreateDto;
import com.encore.space.domain.post.dto.PostDetailResDto;
import com.encore.space.domain.post.dto.PostListDto;
import com.encore.space.domain.post.dto.PostUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChangeType {

    // 시큐리티에 있는 것을 가져다 쓰면 순환 참조가 걸림 조심.
    private final PasswordConfig passwordConfig;


    @Autowired
    public ChangeType(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }


    public MemberResDto memberTOmemberResDto(Member member){
        return MemberResDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .loginType(member.getLoginType())
                .build();
    }

    public Member memberReqDtoTOmember(MemberReqDto memberReqDto){
        return Member.builder()
                .name(memberReqDto.getName())
                .email(memberReqDto.getEmail())
                .profile(memberReqDto.getProfile())
                .password(memberReqDto.getPassword() == null ? null : passwordConfig.passwordEncoder().encode(memberReqDto.getPassword()))
                .nickname(memberReqDto.getNickname())
                .loginType(memberReqDto.getLoginType())
                .build();
    }

    public Post postCreateDtoToPost(PostCreateDto postCreateDto,Member member){
        return Post.builder()
                .title(postCreateDto.getTitle())
                .contents(postCreateDto.getContents())
                .member(member)
                .build();
    }

    public PostDetailResDto postToPostDetailResDto(Post post, Long postHearts){
        List<String> filePath = new ArrayList<>();
        for (AttachFile a : post.getAttachFiles()) {
            filePath.add(a.getAttachFilePath());
        }
        PostDetailResDto postDetailDto = PostDetailResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .nickname(post.getMember().getNickname())
                .postStatus(post.getPostStatus())
                .attachFiles(filePath)
                .postHearts(postHearts)
                .build();
        return postDetailDto;
    }

    public PostListDto postToPostListDto(Post post){
        PostListDto postListDto = PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .nickname(post.getMember().getNickname())
                .createdTime(post.getCreated_at())
                .updatedTime(post.getUpdated_at())
                .postStatus(post.getPostStatus())
                .build();
        return postListDto;
    }


}
