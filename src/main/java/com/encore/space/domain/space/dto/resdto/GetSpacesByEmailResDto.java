package com.encore.space.domain.space.dto.resdto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetSpacesByEmailResDto {
    private final Long SpaceId;
    private final String spaceName;
    private final String spaceType;
    private final String description;
    private final String spaceThumbNailPath;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;

}
