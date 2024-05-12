package com.encore.space.domain.space.service;

import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.repository.PostRepository;
import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.schedule.repository.ScheduleRepository;
import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.domain.SpaceMember;
import com.encore.space.domain.space.domain.SpaceRole;
import com.encore.space.domain.space.domain.SpaceType;
import com.encore.space.domain.space.dto.reqdto.CreateSpaceReqDto;
import com.encore.space.domain.space.dto.resdto.*;
import com.encore.space.domain.space.repository.SpaceMemberRepository;
import com.encore.space.domain.space.repository.SpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepo;
    private final SpaceMemberRepository spaceMemberRepo;
    private final ScheduleRepository scheduleRepo;
    private final PostRepository postRepo;
    private final MemberService memberService;
    private final ChangeType changeType;

    @Autowired
    public SpaceService(SpaceRepository spaceRepo, SpaceMemberRepository spaceMemberRepo,
                        MemberService memberService, ScheduleRepository scheduleRepo,
                        PostRepository postRepo, ChangeType changeType) {
        this.spaceRepo = spaceRepo;
        this.memberService = memberService;
        this.spaceMemberRepo = spaceMemberRepo;
        this.scheduleRepo = scheduleRepo;
        this.postRepo = postRepo;
        this.changeType = changeType;
    }

    //    함수 공통화
    public Space findSpaceBySapceId(Long spaceId) {
        return spaceRepo.findById(spaceId)
                .orElseThrow(() -> new EntityNotFoundException("찾으시는 스페이스가 없습니다."));
    }

    //    Create
    public CreateSpaceResDto createSpaceWithMembers(SpaceType spaceType, CreateSpaceReqDto dto) {
        Space space = changeType.makeReqDtoTOSpace(spaceType, dto);

        Map<Member, SpaceRole> members = new HashMap<>();
        for (CreateSpaceReqDto.MemberEmailAndSpaceRole memberDto : dto.getSpaceMembers()) {
            String email = memberDto.getMemberEmail();
            Member member = memberService.findByEmail(email);
            SpaceRole spaceRole = SpaceRole.CAPTAIN;
            if (!memberDto.getSpaceRole().equals("CAPTAIN")) spaceRole = SpaceRole.CREW;
            members.put(member, spaceRole);
        }

        List<SpaceMember> spaceMembers = new ArrayList<>();
        for (Map.Entry<Member, SpaceRole> entry : members.entrySet()) {
            SpaceMember spaceMember =
                    SpaceMember.builder()
                            .space(space)
                            .member(entry.getKey())
                            .spaceRole(entry.getValue())
                            .build();
            spaceMembers.add(spaceMember);
        }
        space.setSpaceMembers(spaceMembers);
        Space savedSpace = spaceRepo.save(space);
        return changeType.spaceTOcreateSpaceResDto(savedSpace, spaceMembers);
    }

    //    Read
    public List<MembersBySpaceResDto> getMembersBySpaceId(Long spaceId) {
        Space space = findSpaceBySapceId(spaceId);
        List<SpaceMember> spaceMembers = spaceMemberRepo.findBySpace(space);
        return spaceMembers.stream()
                .filter(spaceMember -> spaceMember.getDelYn().equals("N"))
                .map(changeType::spaceMemberTOmembersBySpaceResDto)
                .collect(Collectors.toList());
    }

    public List<SchedulesBySpaceResDto> getSchedulesBySpaceId(Long spaceId) {
        Space space = findSpaceBySapceId(spaceId);
        List<Schedule> schedules = scheduleRepo.findBySpace(space);
        return schedules.stream()
                .map(changeType::scheduleTOschedulesBySpaceResDto)
                .collect(Collectors.toList());
    }

    public List<PostsBySpaceResDto> getPostsBySpaceId(Long spaceId) {
        Space space = findSpaceBySapceId(spaceId);
        List<Post> posts = postRepo.findBySpace(space);
        return posts.stream()
                .filter(post -> post.getDelYN().equals("N"))
                .map(changeType::postsTOpostsBySpaceResDto)
                .collect(Collectors.toList());
    }

    public List<GetSpacesByEmailResDto> getSpacesByEamil() {
        Member member = memberService.getMemberByAuthetication();
        List<SpaceMember> spaceMembers = spaceMemberRepo.findByMember(member);
        return spaceMembers.stream()
                .map(changeType::spaceMemberTOgetSpacesByEmailResDto)
                .collect(Collectors.toList());
    }

    public List<GetAllSpacesResDto> getAllSpaces() {
        List<Space> spaces = spaceRepo.findAll();
        return spaces.stream()
                .map(changeType::spaceTOgetAllSpaceResDto)
                .collect(Collectors.toList());
    }

}
