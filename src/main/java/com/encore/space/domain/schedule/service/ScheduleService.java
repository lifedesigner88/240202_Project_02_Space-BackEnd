package com.encore.space.domain.schedule.service;

import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.repository.MemberRepository;
import com.encore.space.domain.schedule.domain.Attendance;
import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.schedule.domain.ScheduleMember;
import com.encore.space.domain.schedule.dto.reqdto.CreateScheduleReqDto;
import com.encore.space.domain.schedule.dto.resdto.CreateScheduleResDto;
import com.encore.space.domain.schedule.repository.ScheduleMemberRepository;
import com.encore.space.domain.schedule.repository.ScheduleRepository;
import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.repository.SpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final SpaceRepository spaceRepo;
    private final ScheduleRepository scheduleRep;
    private final ScheduleMemberRepository scheduleMemberRepo;
    private final MemberRepository memberRepository;

    @Autowired
    public ScheduleService(SpaceRepository spaceRepo, ScheduleRepository scheduleRep, ScheduleMemberRepository scheduleMemberRepo, MemberRepository memberRepository) {
        this.spaceRepo = spaceRepo;
        this.scheduleRep = scheduleRep;
        this.scheduleMemberRepo = scheduleMemberRepo;
        this.memberRepository = memberRepository;
    }

    public CreateScheduleResDto createSchedule(String spaceId, String email, CreateScheduleReqDto dto) {
        Space space = spaceRepo.findById(Long.parseLong(spaceId))
                .orElseThrow(() -> new EntityNotFoundException("해당 스페이스는 더이상 존재하지 않습니다."));
        Schedule schedule = dto.makeReqDtoToSchedule();
        schedule.setSpace(space);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(email + " : 이 이메일은 존재하지 않는 이메일."));

        ScheduleMember scheduleMember = ScheduleMember.builder()
                .schedule(schedule)
                .member(member)
                .attendance(Attendance.OWNER)
                .build();

        schedule.addScheduleMember(scheduleMember);
        Schedule savedSchedule = scheduleRep.save(schedule);
        return new CreateScheduleResDto(savedSchedule);


    }
}
