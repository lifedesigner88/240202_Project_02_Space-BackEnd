package com.encore.space.domain.space.dto.resdto;

import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.domain.SpaceMember;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetSpacesByEmailResDto {
    private final Long SpaceId;
    private final String spaceName;
    private final String spaceType;
    private final String description;
    private final String spaceThumbNailPath;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    public GetSpacesByEmailResDto (SpaceMember spaceMember){
        Space space = spaceMember.getSpace();
        this.SpaceId = space.getId();
        this.spaceName = space.getSpaceName();
        this.spaceType = space.getSpaceType().toString();
        this.description = space.getDescription();
        this.spaceThumbNailPath = space.getSpaceThumbNailPath();
        this.created_at = space.getCreated_at();
        this.updated_at = space.getUpdated_at();
    }
}
