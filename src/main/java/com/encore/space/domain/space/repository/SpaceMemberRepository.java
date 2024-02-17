package com.encore.space.domain.space.repository;

import com.encore.space.domain.space.domain.SpaceMember;
import com.encore.space.domain.space.domain.SpaceMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceMemberRepository extends JpaRepository<SpaceMember, SpaceMemberId> {

}
