package com.encore.space.domain.chat.controller;

import com.encore.space.domain.chat.dto.ChatDto;
import com.encore.space.domain.chat.dto.ChatDto.MessageType;
import com.encore.space.domain.chat.repository.ChatRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    // convertAndSend를 사용하기 위해 선언.
    // convertAndSend 메서드는 매개변수로 메시지의 도착지와 ChatDto 객체를 넣어주면
    // 인자로 들어온 객체를 Message 객체로 변환해서 도착지를 subscribe 하고 있는 모든 사용자에게 메시지를 보내준다.
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatRepository chatRepository;

    // MessageMapping으로 웹소켓을 통해 들어오는 메시지를 처리.
    // 클라이언트에서는 /pub/chat/enterUser로 요청하고 controller에서 받아서 처리.
    // convertAndSend로 /sub/chat/room/roomId 로 메시지 전송
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {
        // 채팅방 유저 1 증가
        chatRepository.plusUserCount(chatDto.getRoomId());

        // 채팅방 유저 추가 및 UserUUID 반환
        String userUUID = chatRepository.addUser(chatDto.getRoomId(), chatDto.getSender());

        // 반환 결과를 세션에 userUUID로 저장
        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chatDto.getRoomId());

        chatDto.setMessage(chatDto.getSender() + "님이 들어왔습니다.");
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDto chatDto) {
        log.info("CHAT {}", chatDto);
        chatDto.setMessage(chatDto.getMessage());
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomId(), chatDto);
    }

    // 사용자가 채팅방에서 퇴장하면 EventListener를 통해서 유저 퇴장을 확인한다.
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Disconnect {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 채팅방에서 퇴장하면 STOMP 세션에 있던 UUID와 roomId를 확인해서
        // 채팅방 유저 리스트와 Room에서 해당 유저를 삭제한다.
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        // 채팅방 유저에서 1명 줄이기
        chatRepository.minusUserCount(roomId);

        // 채팅방 유저 리스트에서 유저를 삭제.
        chatRepository.deleteUser(roomId, userUUID);

        String userName = chatRepository.getUserName(roomId, userUUID);
        if (userName != null) {
            log.info("User Disconnected : {}", userName);

            ChatDto chat = ChatDto.builder()
                    .type(MessageType.LEAVE)
                    .sender(userName)
                    .message(userName + "님이 나갔습니다.")
                    .build();
            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userList")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {
        return chatRepository.getUserList(roomId);
    }

    // 채팅에 참여하는 유저의 닉네임 중복 확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("userName") String userName) {
        String duplicateName = chatRepository.isDuplicateName(roomId, userName);
        return duplicateName;
    }
}
