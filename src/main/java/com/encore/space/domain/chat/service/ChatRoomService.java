package com.encore.space.domain.chat.service;

import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.dto.ChatRoomCreateDto;
import com.encore.space.domain.chat.repository.ChatRoomRepository;
import com.encore.space.domain.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createRoom(String name, Member member) {
        ChatRoomCreateDto dto = new ChatRoomCreateDto().create(name);
        ChatRoom chatRoom = ChatRoom.toChatRoom(name, dto.getRoomId(), member.getNickname());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findRoomByRoomId(String roomId) {
        return getRoomByRoomId(roomId);
    }

    private ChatRoom getRoomByRoomId(String roomId) {
        Optional<ChatRoom> room = chatRoomRepository.findByRoomId(roomId);

        if (room.isPresent()) {
            return room.get();
        }
        throw new EntityNotFoundException("채팅룸을 찾을 수 없습니다.");
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public void deleteRoom(ChatRoom chatRoom) {
        log.info("제거된 방 {}", chatRoom.getRoomId());
        chatRoomRepository.delete(chatRoom);
    }

    public void checkDuplicateRoom(String name) {
        if (chatRoomRepository.findByRoomName(name).isPresent()) {
            throw new IllegalArgumentException("중복된 채팅룸이 존재합니다.");
        }
    }



}
