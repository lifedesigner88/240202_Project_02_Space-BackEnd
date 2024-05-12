package com.encore.space.domain.space.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@IdClass(SpaceMemberId.class)
public class SpaceMember extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    @Id private Space space;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Id private Member member;


    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SpaceRole spaceRole;

    @Setter
    private String delYn = "N";

    @Builder
    public SpaceMember(Space space, Member member, SpaceRole spaceRole) {
        this.space = space;
        this.member = member;
        this.spaceRole = spaceRole;
    }

    public SpaceMember() {}
}





