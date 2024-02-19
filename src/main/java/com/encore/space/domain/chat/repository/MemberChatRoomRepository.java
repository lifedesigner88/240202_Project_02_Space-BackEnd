package com.encore.space.domain.chat.repository;

import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.domain.MemberChatRoom;
import com.encore.space.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {
    Optional<MemberChatRoom> findByMemberAndChatroom(Member member, ChatRoom chatRoom);
}
