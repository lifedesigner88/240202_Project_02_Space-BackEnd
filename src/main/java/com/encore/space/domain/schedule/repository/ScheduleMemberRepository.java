package com.encore.space.domain.schedule.repository;

import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.schedule.domain.ScheduleMember;
import com.encore.space.domain.schedule.domain.ScheduleMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, ScheduleMemberId> {
    List<ScheduleMember> findBySchedule(Schedule schedule);
    List<ScheduleMember> findByMember(Member member);

    Optional<ScheduleMember> findByScheduleAndMember(Schedule schedule, Member member);

}
