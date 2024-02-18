package com.encore.space.domain.space.dto.resdto;

import com.encore.space.domain.space.domain.Space;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAllSpacesResDto {
    private final Long SpaceId;
    private final String spaceName;
    private final String spaceType;
    private final String description;
    private final String spaceThumbNailPath;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

    public GetAllSpacesResDto (Space space){
        this.SpaceId = space.getId();
        this.spaceName = space.getSpaceName();
        this.spaceType = space.getSpaceType().toString();
        this.description = space.getDescription();
        this.spaceThumbNailPath = space.getSpaceThumbNailPath();
        this.created_at = space.getCreated_at();
        this.updated_at = space.getUpdated_at();
    }
}
