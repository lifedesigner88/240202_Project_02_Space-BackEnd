package com.encore.space.domain.space.dto.reqdto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateSpaceReqDto {
    private String spaceName;
    private String description;
    private String spaceThumbNailPath;
    private List<MemberEmailAndSpaceRole> spaceMembers;

    @Getter
    @Setter
    public static class MemberEmailAndSpaceRole {
        private String memberEmail;
        private String spaceRole;
    }

}
