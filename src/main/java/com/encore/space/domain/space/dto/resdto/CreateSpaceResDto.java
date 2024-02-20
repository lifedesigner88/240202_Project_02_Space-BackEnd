package com.encore.space.domain.space.dto.resdto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateSpaceResDto {
    private final String spaceName;
    private final String spaceType;
    private final String description;
    private final String spaceThumbNailPath;
    private final List<MembersBySpaceResDto> spaceMembers;
}

