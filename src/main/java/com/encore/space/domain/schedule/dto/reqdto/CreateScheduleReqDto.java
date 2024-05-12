package com.encore.space.domain.schedule.dto.reqdto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateScheduleReqDto {
    private String scheduleTitle;
    private String startDateTime;
    private String endDateTime;
    private String description;
    private String isKanBan;
}
