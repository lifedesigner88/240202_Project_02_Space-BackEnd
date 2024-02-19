package com.encore.space.domain.chat.service;

import com.encore.space.domain.chat.domain.Chat;
import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.domain.MessageType;
import com.encore.space.domain.chat.dto.ChatReqDto;
import com.encore.space.domain.chat.repository.ChatRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;

    @Transactional
    public Chat save(ChatReqDto message, MessageType messageType) {
        return chatRepository.save(
                Chat.toChat(message, chatRoomService.findRoomByRoomId(message.getRoomId()), messageType));
    }

    public List<Chat> findAllChatByRoomIdAndDate(ChatRoom chatRoom, LocalDateTime subscribeDateTime) {
        return chatRepository.findAllBySendAtAfterAndChatroom(subscribeDateTime, chatRoom);
    }

    public List<Chat> findAllChat(ChatRoom chatRoom) {
        return chatRepository.findAllById(chatRoom.getId());
    }

}
