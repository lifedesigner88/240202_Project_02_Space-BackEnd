package com.encore.space.domain.chat.service;

import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.domain.MemberChatRoom;
import com.encore.space.domain.chat.repository.MemberChatRoomRepository;
import com.encore.space.domain.member.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberChatRoomService {
    private final MemberChatRoomRepository memberChatRoomRepository;

    @Transactional
    public MemberChatRoom subscribe(Member member, ChatRoom chatRoom) {
        return memberChatRoomRepository.save(new MemberChatRoom(member, chatRoom));
    }

    @Transactional
    public void unSubscribe(Member member, ChatRoom chatRoom) {
        MemberChatRoom memberChatroom = findMemberChatroom(member, chatRoom);
        memberChatroom.delete(memberChatroom);
        memberChatRoomRepository.deleteById(memberChatroom.getId());
    }

    public MemberChatRoom findMemberChatroom(Member member, ChatRoom chatRoom) {
        Optional<MemberChatRoom> memberChatRoom = memberChatRoomRepository.findByMemberAndChatroom(member, chatRoom);
        if (memberChatRoom.isPresent()) {
            return memberChatRoom.get();
        }
        // 채팅방을 구독하고 있지 않은 경우 bad request
        throw new IllegalArgumentException("채팅방을 구독하고 있지 않습니다.");
    }

    public boolean checkMemberChatroom(Member member, ChatRoom chatRoom) {
        return memberChatRoomRepository.findByMemberAndChatroom(member, chatRoom).isPresent();
    }
}
