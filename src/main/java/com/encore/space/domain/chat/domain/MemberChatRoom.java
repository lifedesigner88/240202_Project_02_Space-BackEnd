package com.encore.space.domain.chat.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.encore.space.domain.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Schema(description = "chat_room과 member의 중간 테이블")
public class MemberChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatroom;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    private void setMember(Member member) {
        member.getMemberChatRooms().add(this);
        this.member = member;
    }

    private void setChatroom(ChatRoom chatRoom) {
        chatRoom.getCurrentSubscriber().add(this);
        this.chatroom = chatRoom;
    }

    public MemberChatRoom(Member member, ChatRoom chatroom) {
        setMember(member);
        setChatroom(chatroom);
    }

    public void delete(MemberChatRoom memberChatRoom) {
        memberChatRoom.member.getMemberChatRooms().remove(this);
        memberChatRoom.chatroom.getCurrentSubscriber().remove(this);
    }

}
