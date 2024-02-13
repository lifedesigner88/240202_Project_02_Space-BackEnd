package com.encore.space.domain.chat.controller;

import com.encore.space.domain.chat.dto.ChatRoomDto;
import com.encore.space.domain.chat.repository.ChatRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
// 내용 수정 및 채우기 필요
public class ChatRoomController {

    @Autowired
    private ChatRepository chatRepository;

    // /message 로 요청이 들어오면 전체 채팅룸 리스트를 담아서 return
    @GetMapping("/message")
    public List<ChatRoomDto> listChatRoom(ChatRoomDto chatRoomDto) {
        return chatRepository.findAllChatRoom();
    }

    // 채팅방 생성
    @PostMapping("/message/room/create")
    public String createChatRoom(ChatRoomDto chatRoomDto) {
        return "ok";
    }

    // 비밀번호 확인
    @PostMapping("/message/confirm")
    public boolean confirmPassword(ChatRoomDto chatRoomDto) {
        String roomId = chatRoomDto.getRoomId();
        String roomPassword = chatRoomDto.getRoomPassword();
        return chatRepository.confirmPassword(roomId, roomPassword);
    }

    // 채팅방 삭제
    @DeleteMapping("/message/room/{id}/delete")
    public String deleteChatRoom(@PathVariable Long id, ChatRoomDto chatRoomDto) {
        String roomId = chatRoomDto.getRoomId();
        chatRepository.deleteChatRoom(roomId);
        return "ok";
    }

}
