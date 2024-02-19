package com.encore.space.domain.schedule.service;

import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.schedule.domain.Attendance;
import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.schedule.domain.ScheduleMember;
import com.encore.space.domain.schedule.dto.reqdto.CreateScheduleReqDto;
import com.encore.space.domain.schedule.dto.resdto.CreateScheduleResDto;
import com.encore.space.domain.schedule.repository.ScheduleRepository;
import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRep;
    private final SpaceService spaceService;
    private final MemberService memberService;
    private final ChangeType changeType;


    @Autowired
    public ScheduleService(ScheduleRepository scheduleRep, SpaceService spaceService,
                           MemberService memberService, ChangeType changeType) {
        this.scheduleRep = scheduleRep;
        this.spaceService = spaceService;
        this.memberService = memberService;
        this.changeType = changeType;
    }

    public CreateScheduleResDto createSchedule(String spaceId, CreateScheduleReqDto dto) {
        Space space = spaceService.findSpaceBySapceId(Long.valueOf(spaceId));
        Schedule schedule = changeType.makeReqDtoTOschedule(dto);
        schedule.setSpace(space);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberService.findByEmail(email);

        ScheduleMember scheduleMember = ScheduleMember.builder()
                .schedule(schedule)
                .member(member)
                .attendance(Attendance.OWNER)
                .build();

        schedule.addScheduleMember(scheduleMember);
        Schedule savedSchedule = scheduleRep.save(schedule);
        return changeType.scheduleToCreateScheduleResDto(savedSchedule);
    }
}
