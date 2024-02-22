package com.encore.space.common.domain;


import com.encore.space.common.config.PasswordConfig;
import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.comment.dto.CommentChildrenListDto;
import com.encore.space.domain.comment.dto.CommentCreateDto;
import com.encore.space.domain.comment.dto.CommentResDto;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.dto.PostCreateDto;
import com.encore.space.domain.post.dto.PostDetailResDto;
import com.encore.space.domain.post.dto.PostListDto;
import com.encore.space.domain.schedule.domain.KanBanStatus;
import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.schedule.dto.reqdto.CreateScheduleReqDto;
import com.encore.space.domain.schedule.dto.resdto.CreateScheduleResDto;
import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.domain.SpaceMember;
import com.encore.space.domain.space.domain.SpaceType;
import com.encore.space.domain.space.dto.reqdto.CreateSpaceReqDto;
import com.encore.space.domain.space.dto.resdto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
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


    public MemberResDto memberTOmemberResDto(Member member) {
        return MemberResDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .loginType(member.getLoginType())
                .build();
    }

    public Member memberReqDtoTOmember(MemberReqDto memberReqDto) {
        return Member.builder()
                .name(memberReqDto.getName())
                .email(memberReqDto.getEmail())
                .profile(memberReqDto.getProfile())
                .password(memberReqDto.getPassword() == null ? null : passwordConfig.passwordEncoder().encode(memberReqDto.getPassword()))
                .nickname(memberReqDto.getNickname())
                .loginType(memberReqDto.getLoginType())
                .build();
    }


    public Post postCreateDtoTOPost(PostCreateDto postCreateDto, Member member, Space space) {
        return Post.builder()
                .title(postCreateDto.getTitle())
                .contents(postCreateDto.getContents())
                .member(member)
                .space(space)
                .build();
    }

    public PostDetailResDto postTOPostDetailResDto(Post post, Long postHearts, int commentCounts) {
        List<String> filePath = new ArrayList<>();
        for (AttachFile a : post.getAttachFiles()) {
            filePath.add(a.getAttachFilePath());
        }
        return PostDetailResDto.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .nickname(post.getMember().getNickname())
                .postStatus(post.getPostStatus().toString())
                .attachFiles(filePath)
                .spaceName(post.getSpace().getSpaceName())
                .spaceType(post.getSpace().getSpaceType().toString())
                .postHearts(postHearts)
                .commentCounts(commentCounts)
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .build();
    }

    public PostListDto postTOPostListDto(Post post) {
        return PostListDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .nickname(post.getMember().getNickname())
                .thumbnail(post.getThumbnail())
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .postStatus(post.getPostStatus())
                .spaceId(post.getSpace().getId())
                .build();
    }

    public Comment commentCreateDtoTOComment(CommentCreateDto commentCreateDto,
                                             Member member,
                                             Post post,
                                             Comment parentComment){
        return Comment.builder()
                .post(post)
                .member(member)
                .content(commentCreateDto.getContents())
                .parentComment(parentComment)
                .build();
    }

    public CommentResDto commentTOCommentResDto(Comment comment, Long commentHearts) {
        return CommentResDto.builder()
                .nickname(comment.getMember().getNickname())
                .contents(comment.getContent())
                .commentHearts(commentHearts)
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .build();
    }

    public CommentChildrenListDto commentTOChildrenListDto(Comment comment) {
        return CommentChildrenListDto.builder()
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdTime(comment.getCreated_at())
                .updatedTime(comment.getUpdated_at())
                .build();
    }

    public AttachFile toAttachFile(MultipartFile m,
                                   Post post,
                                   Path path,
                                   String isThumbnail){
        return AttachFile.builder()
                .post(post)
                .attachFileName(m.getOriginalFilename())
                .fileSize(m.getSize())
                .fileType(m.getContentType())
                .thumbnail(isThumbnail)
                .attachFilePath(path.toString())
                .build();
    }

    //    스케쥴 관련
    public Schedule makeReqDtoTOschedule(CreateScheduleReqDto dto) {

        return Schedule.builder()
                .scheduleTitle(dto.getScheduleTitle())
                .startDateTime(LocalDateTime.parse(dto.getStartDateTime()))
                .endDateTime(LocalDateTime.parse(dto.getEndDateTime()))
                .description(dto.getDescription())
                .status(KanBanStatus.TODO)
                .isKanBan(dto.getIsKanBan())
                .build();
    }

    public CreateScheduleResDto scheduleToCreateScheduleResDto(Schedule schedule) {
        Space space = schedule.getSpace();
        return CreateScheduleResDto.builder()
                .savedSpaceId(space.getId())
                .savedSpaceType(space.getSpaceType().toString())
                .savedSpacetitle(space.getSpaceName())
                .savedspaceThumbNailPath(space.getSpaceThumbNailPath())
                .scheduleId(schedule.getId())
                .whoCreate(schedule.getScheduleMembers().get(0).getMember().getEmail())
                .scheduleTitle(schedule.getScheduleTitle())
                .startDateTime(schedule.getStartDateTime().toString())
                .endDateTime(schedule.getEndDateTime().toString())
                .description(schedule.getDescription())
                .isKanBan(schedule.getIsKanBan())
                .build();
    }


    //    스페이스 관련
    public Space makeReqDtoTOSpace(SpaceType spaceType, CreateSpaceReqDto dto) {
        return Space.builder()
                .spaceName(dto.getSpaceName())
                .spaceType(spaceType)
                .description(dto.getDescription())
                .spaceThumbNailPath(dto.getSpaceThumbNailPath() + (10 + (int) (Math.random() * 90)))
                .build();

    }

    public CreateSpaceResDto spaceTOcreateSpaceResDto(Space space, List<SpaceMember> spaceMembers) {
        return CreateSpaceResDto.builder()
                .spaceName(space.getSpaceName())
                .spaceType(space.getSpaceType().toString())
                .description(space.getDescription())
                .spaceThumbNailPath(space.getSpaceThumbNailPath())
                .spaceMembers(
                        spaceMembers
                                .stream()
                                .map(this::spaceMemberTOmembersBySpaceResDto)
                                .toList()
                ).build();
    }

    public MembersBySpaceResDto spaceMemberTOmembersBySpaceResDto(SpaceMember spaceMember) {
        Member member = spaceMember.getMember();
        return MembersBySpaceResDto.builder()
                .role(spaceMember.getSpaceRole().name())
                .name(member.getName())
                .nickName(member.getNickname())
                .email(member.getEmail())
                .build();
    }

    public SchedulesBySpaceResDto scheduleTOschedulesBySpaceResDto(Schedule schedule) {
        Member member = schedule.getScheduleMembers().get(0).getMember();
        return SchedulesBySpaceResDto.builder()
                .scheduleId(schedule.getId())
                .whoCreate(member.getName())
                .whosNickName(member.getNickname())
                .whosEmail(member.getEmail())
                .scheduleTitle(schedule.getScheduleTitle())
                .startDateTime(schedule.getStartDateTime().toString())
                .endDateTime(schedule.getEndDateTime().toString())
                .description(schedule.getDescription())
                .isKanBan(schedule.getIsKanBan())
                .build();
    }

    public PostsBySpaceResDto postsTOpostsBySpaceResDto(Post post) {

        Member member = post.getMember();
        Space space = post.getSpace();
        return PostsBySpaceResDto.builder()
                .spaceType(space.getSpaceType().toString())
                .AuthorId(member.getId())
                .AuthorEmail(member.getEmail())
                .AuthorName(member.getName())
                .AuthorNickName(member.getNickname())
                .postId(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .build();
    }

    public GetSpacesByEmailResDto spaceMemberTOgetSpacesByEmailResDto(SpaceMember spaceMember) {
        Space space = spaceMember.getSpace();
        return GetSpacesByEmailResDto.builder()
                .SpaceId(space.getId())
                .spaceName(space.getSpaceName())
                .spaceType(space.getSpaceType().toString())
                .description(space.getDescription())
                .spaceThumbNailPath(space.getSpaceThumbNailPath())
                .created_at(space.getCreated_at())
                .updated_at(space.getUpdated_at())
                .build();
    }

    public GetAllSpacesResDto spaceTOgetAllSpaceResDto(Space space) {
        return GetAllSpacesResDto.builder()
                .SpaceId(space.getId())
                .spaceName(space.getSpaceName())
                .spaceType(space.getSpaceType().toString())
                .description(space.getDescription())
                .spaceThumbNailPath(space.getSpaceThumbNailPath())
                .created_at(space.getCreated_at())
                .updated_at(space.getUpdated_at())
                .build();

    }
}
