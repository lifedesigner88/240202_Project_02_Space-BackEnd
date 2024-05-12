package com.encore.space.domain.chat.controller;

import com.encore.space.domain.chat.domain.Chat;
import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.domain.MemberChatRoom;
import com.encore.space.domain.chat.domain.MessageType;
import com.encore.space.domain.chat.dto.ChatReqDto;
import com.encore.space.domain.chat.dto.ChatResDto;
import com.encore.space.domain.chat.service.ChatRoomService;
import com.encore.space.domain.chat.service.ChatService;
import com.encore.space.domain.chat.service.MemberChatRoomService;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹소켓 통신을 위한 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MemberService memberService;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final MemberChatRoomService memberChatRoomService;

    @Operation(
            summary = "WebSocket을 통해서 채팅방에 입장하는 경우의 로직을 처리",
            description = """
                    - 사용자가 채팅방에 입장할 때 "/chat/enter"를 통해서 메시지 요청이 들어오면 현재 메서드가 호출.
                    - 사용자가 최초로 입장하면 입장했음을 알리는 메시지를 채팅방에 보낸다.
                    - memberService와 chatRoomService를 사용해서 사용자와 채팅방의 정보를 가져오고, 시스템은 사용자가 해당 채팅방에 이미 참여하고 있는지 확인한다.
                    - 만약 참여 중이라면, 채팅 기록을 가져와서 이전에 존재하던 채팅 내역을 보여줄 수 있도록 처리한다.
                    - 채팅 내역은 /user/{sender}/sub/chat/enter/{roomId}를 구독하고 있는 모든 사용자들에게 전송된다.
                    - 만약 채팅방에 처음 들어오는 것이라면 사용자를 채팅방에 구독시키고, 사용자가 방에 들어왔다는 메시지를 /sub/chat/{roomId}를 구독하고 있는 모든 사용자들에게 전송한다.
                    """
    )
    @Transactional
    @MessageMapping("/chat/enter")
    public void enter(ChatReqDto message) {
        log.info("enter: {}", message);
        message.setMessage(message.getSender() + "님이 입장했습니다.");

        Member member = memberService.findByNickname(message.getSender());
        ChatRoom chatRoom = chatRoomService.findRoomByRoomId(message.getRoomId());

        if (memberChatRoomService.checkMemberChatroom(member, chatRoom)) {
            // 특정 채팅룸의 이전 채팅 기록 가져오기
            List<ChatResDto> chatList = new ArrayList<>();
            MemberChatRoom memberChatroom = memberChatRoomService.findMemberChatroom(member, chatRoom);
            List<Chat> allChatList = chatService.findAllChatByRoomIdAndDate(chatRoom,
                    memberChatroom.getJoinedAt());
            if (allChatList != null) {
                allChatList.stream().map(ChatResDto::convertToDto).collect(Collectors.toList());
            }
            // 해당 URL을 구독하고 있는 사람들에게 전송
            simpMessagingTemplate.convertAndSend(
                    message.getSender() + "/sub/chat/" + message.getRoomId(),
                    chatList
            );

            log.info("지난 기록 전송했다! : {}", chatList);
        } else {
            log.info("구독합니다~");
            memberChatRoomService.subscribe(member, chatRoom);

            // 입장 메시지 추가
            Chat save = chatService.save(message, MessageType.ENTER);
            ChatResDto enterNow = ChatResDto.convertToDto(save);
            simpMessagingTemplate.convertAndSend("/sub/chat/" + message.getRoomId(), enterNow);
        }
    }

    @Operation(
            summary = "사용자가 채팅룸은 나갔지만, 구독을 취소하지 않은 경우",
            description = "구체적인 로직을 처리하지 않고, 요청 정보를 로그로 출력"
    )
    @MessageMapping("/chat/out")
    public void out(ChatReqDto message) {
        log.info("out: {}", message);
    }

    @Operation(
            summary = "사용자가 채팅방을 나가면서 해당 채팅방에 대한 구독도 취소하는 경우",
            description = """
                    - 사용자가 채팅방을 나갈 때, 사용자의 닉네임을 이용해서 사용자 정보를, 그리고 채팅방의 ID를 이용해서 채팅방 정보를 불러온다.
                    - 사용자를 해당 채팅방에서 구독 해지시킨다.
                    - 사용자 정보와 메시지 타입을 입력해서 채팅 메시지를 저장한다.
                    - 저장된 메시지는 채팅방을 구독 중인 모든 사용자에게 convertAndSend를 통해서 전송한다.
                    - 현재 채팅방에 아무도 없다면 채팅방을 삭제한다.
                    """
    )
    @Transactional
    @MessageMapping("/chat/subscribe/out")
    public void subscribeOut(ChatReqDto message) {
        log.info("out: {}", message);

        Member member = memberService.findByNickname(message.getSender());
        ChatRoom room = chatRoomService.findRoomByRoomId(message.getRoomId());

        memberChatRoomService.unSubscribe(member, room);

        message.setMessage(message.getSender() + "님이 퇴장했습니다.");
        Chat exitChat = chatService.save(message, MessageType.EXIT);
        ChatResDto chatResDto = ChatResDto.convertToDto(exitChat);

        // 채팅룸에 아무도 없으면 해당 채팅룸은 모든 사용자가 나간 것으로 간주하고 삭제한다.
        if (room.getCurrentSubscriber().isEmpty()) {
            chatRoomService.deleteRoom(room);
        }

        simpMessagingTemplate.convertAndSend("/sub/chat/" + message.getRoomId(), chatResDto);
    }

    @Operation(
            summary = "메시지 전송 및 DB 저장",
            description = "구독 중인 채팅룸으로 메시지를 전송과 동시에 DB에 채팅 메시지를 저장"
    )
    @MessageMapping("/chat/send/{roomId}")
    @SendTo("/sub/chat/send/{roomId}")
    public ChatResDto sendMessage(@DestinationVariable String roomId, ChatReqDto messageData) {
        Chat createdChat = chatService.save(messageData, MessageType.CHAT);
        ChatResDto chatResDto = ChatResDto.convertToDto(createdChat);
        // 받은 메시지를 그대로 다시 전송합니다.
        return chatResDto;
    }
}
