package com.encore.space.domain.schedule.dto.reqdto;

import com.encore.space.domain.schedule.domain.KanBanStatus;
import com.encore.space.domain.schedule.domain.Schedule;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
public class CreateScheduleReqDto {
    private String scheduleTitle;
    private String startDateTime;
    private String endDateTime;
    private String description;
    private String isKanBan;

    public Schedule makeReqDtoToSchedule(){
        return Schedule.builder()
                .scheduleTitle(scheduleTitle)
                .startDateTime(LocalDateTime.parse(startDateTime))
                .endDateTime(LocalDateTime.parse(endDateTime))
                .description(description)
                .status(KanBanStatus.TODO)
                .isKanBan(isKanBan)
                .build();
    }
}
