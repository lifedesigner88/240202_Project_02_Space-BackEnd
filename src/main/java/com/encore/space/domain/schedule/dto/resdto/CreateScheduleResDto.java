package com.encore.space.domain.schedule.dto.resdto;

import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.space.domain.Space;
import lombok.Getter;

@Getter
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


    public CreateScheduleResDto (Schedule schedule) {

        Space space = schedule.getSpace();
        this.savedSpaceId = space.getId();
        this.savedSpaceType = space.getSpaceType().toString();
        this.savedSpacetitle = space.getSpaceName();
        this.savedspaceThumbNailPath = space.getSpaceThumbNailPath();

        this.scheduleId = schedule.getId();
        this.whoCreate = schedule.getScheduleMembers().get(0).getMember().getEmail();
        this.scheduleTitle = schedule.getScheduleTitle();
        this.startDateTime = schedule.getStartDateTime().toString();
        this.endDateTime = schedule.getEndDateTime().toString();
        this.description = schedule.getDescription();
        this.isKanBan = schedule.getIsKanBan();
    }

}
