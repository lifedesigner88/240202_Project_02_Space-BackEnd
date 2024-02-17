package com.encore.space.domain.space.domain;

import com.encore.space.domain.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class SpaceMemberId implements Serializable {

    private Space space;
    private Member member;

}
