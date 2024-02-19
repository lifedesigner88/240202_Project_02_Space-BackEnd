package com.encore.space.domain.schedule.dto.resdto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateScheduleResDto {

    private final Long savedSpaceId;
    private final String savedSpaceType;
    private final String savedSpacetitle;
    private final String savedspaceThumbNailPath;

    private final Long scheduleId;
    private final String whoCreate;
    private final String scheduleTitle;
    private final String startDateTime;
    private final String endDateTime;
    private final String description;
    private final String isKanBan;

}
