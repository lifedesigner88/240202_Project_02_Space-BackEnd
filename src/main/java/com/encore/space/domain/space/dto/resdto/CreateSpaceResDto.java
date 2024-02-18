package com.encore.space.domain.space.dto.resdto;

import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.domain.SpaceMember;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CreateSpaceResDto {
    private final String spaceName;
    private final String description;
    private final String spaceThumbNailPath;
    private final List<MembersBySpaceResDto> spaceMembers;

    public CreateSpaceResDto(Space space, List<SpaceMember> spaceMembers) {
        this.spaceName = space.getSpaceName();
        this.description = space.getDescription();
        this.spaceThumbNailPath = space.getSpaceThumbNailPath();
        this.spaceMembers = spaceMembers.stream()
                .map(MembersBySpaceResDto::new)
                .collect(Collectors.toList());
    }
}

