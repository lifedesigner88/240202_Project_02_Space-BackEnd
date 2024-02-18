package com.encore.space.domain.space.dto.resdto;

import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.schedule.domain.Schedule;
import lombok.Getter;

@Getter
public class SchedulesBySpaceResDto {
    private final Long scheduleId;
    private final String whoCreate;
    private final String whosNickName;
    private final String whosEmail;

    private final String scheduleTitle;
    private final String startDateTime;
    private final String endDateTime;
    private final String description;
    private final String isKanBan;

    public SchedulesBySpaceResDto(Schedule schedule) {

        Member member = schedule.getScheduleMembers().get(0).getMember();
        this.scheduleId = schedule.getId();
        this.whoCreate = member.getName();
        this.whosNickName = member.getNickname();
        this.whosEmail = member.getEmail();

        this.scheduleTitle = schedule.getScheduleTitle();
        this.startDateTime = schedule.getStartDateTime().toString();
        this.endDateTime = schedule.getEndDateTime().toString();
        this.description = schedule.getDescription();
        this.isKanBan = schedule.getIsKanBan();

    }

}
