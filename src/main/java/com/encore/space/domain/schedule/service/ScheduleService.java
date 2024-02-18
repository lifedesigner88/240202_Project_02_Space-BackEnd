package com.encore.space.domain.schedule.service;

import com.encore.space.domain.member.repository.MemberRepository;
import com.encore.space.domain.schedule.repository.ScheduleMemberRepository;
import com.encore.space.domain.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRep;
    private final ScheduleMemberRepository scheduleMemberRepo;
    private final MemberRepository memberRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRep, ScheduleMemberRepository scheduleMemberRepo, MemberRepository memberRepository) {
        this.scheduleRep = scheduleRep;
        this.scheduleMemberRepo = scheduleMemberRepo;
        this.memberRepository = memberRepository;
    }

}
