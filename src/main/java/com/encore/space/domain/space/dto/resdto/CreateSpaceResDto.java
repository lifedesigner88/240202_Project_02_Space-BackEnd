package com.encore.space.domain.space.dto.resdto;

import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.domain.SpaceMember;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateSpaceResDto {
    private final String spaceName;
    private final String description;
    private final String spaceThumbNailPath;
    private final List<MemberInfo> spaceMembers;


    @Getter
    @Builder
    public static class MemberInfo {
        private final String role;
        private final String name;
        private final String nickName;
        private final String email;

    }

    public CreateSpaceResDto(Space space, List<SpaceMember> members) {
        this.spaceName = space.getSpaceName();
        this.description = space.getDescription();
        this.spaceThumbNailPath = space.getSpaceThumbNailPath();
        this.spaceMembers = new ArrayList<>();
        for (SpaceMember member : members)
            this.spaceMembers.add(
                    MemberInfo.builder()
                            .role(member.getSpaceRole().toString())
                            .nickName(member.getMember().getNickname())
                            .name(member.getMember().getName())
                            .email(member.getMember().getEmail())
                            .build());
    }
}
