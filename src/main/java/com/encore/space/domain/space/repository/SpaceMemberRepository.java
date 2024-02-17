package com.encore.space.domain.space.repository;

import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.domain.SpaceMember;
import com.encore.space.domain.space.domain.SpaceMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceMemberRepository
        extends JpaRepository<SpaceMember, SpaceMemberId> {

    List<SpaceMember> findBySpace(Space space);
    List<SpaceMember> findByMember(Member member);

    // 스페이스에서 나갈 때 사용
    SpaceMember findBySpaceAndMember(Space space, Member member);

}
