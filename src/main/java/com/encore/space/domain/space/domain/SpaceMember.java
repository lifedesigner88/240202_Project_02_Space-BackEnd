package com.encore.space.domain.space.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
public class SpaceMember extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    @Id private Space space;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Id private Member member;

    @Column(nullable = false)
    private String spaceName;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SpaceRole spaceRole;

    @Setter
    private String delYn = "N";

    @Builder
    public SpaceMember(Space space, Member member,
                       String spaceName, SpaceRole spaceRole) {
        this.space = space;
        this.member = member;
        this.spaceName = spaceName;
        this.spaceRole = spaceRole;
    }

    public SpaceMember() {}
}
