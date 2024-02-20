package com.encore.space.domain.schedule.domain;

import com.encore.space.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@IdClass(ScheduleMemberId.class)
public class ScheduleMember {

    @ManyToOne
    @JoinColumn(nullable = false)
    @Id
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Id
    private Member member;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Attendance attendance;

    @Builder
    public ScheduleMember(Schedule schedule, Member member, Attendance attendance) {
        this.schedule = schedule;
        this.member = member;
        this.attendance = attendance;
    }

    public ScheduleMember() {
    }
}
