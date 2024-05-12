package com.encore.space.domain.schedule.domain;


import com.encore.space.domain.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class ScheduleMemberId implements Serializable {

    private Schedule schedule;
    private Member member;


}
