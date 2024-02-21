package com.encore.space.domain.chat.domain;

import com.encore.space.domain.chat.dto.ChatReqDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatroom;

    private String sender;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @CreationTimestamp
    private LocalDateTime sendAt;

    //생성자
    public static Chat toChat(ChatReqDto chatReqDto, ChatRoom chatRoom, MessageType messageType) {
        Chat chat = new Chat();
        chat.chatroom = chatRoom;
        chat.sender = chatReqDto.getSender();
        chat.message = chatReqDto.getMessage();
        chat.messageType = messageType;
        return chat;
    }
}
