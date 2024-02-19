package com.encore.space.domain.space.dto.resdto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
}
