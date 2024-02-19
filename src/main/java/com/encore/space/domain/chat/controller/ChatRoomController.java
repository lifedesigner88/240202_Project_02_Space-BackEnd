package com.encore.space.domain.chat.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.dto.ChatRoomDetailDto;
import com.encore.space.domain.chat.service.ChatRoomService;
import com.encore.space.domain.login.domain.CustomUserDetails;
import com.encore.space.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @GetMapping("/chat/room")
    public ResponseEntity<CommonResponse> findAllRooms() {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전체 채팅룸 검색 성공",
                chatRoomService.findAllRoom()
        );
    }

    // ChatRoomDetailDto 반환하도록 수정할 것.
    @GetMapping("/chat/room/{roomId}")
    public ResponseEntity<CommonResponse> findRoomById(@PathVariable("roomId") String roomId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "채팅룸 id=" + roomId + " 검색 성공",
                chatRoomService.findRoomByRoomId(roomId)
        );
    }

    @PostMapping("/chat/room/{name}")
    public ResponseEntity<CommonResponse> createRoom(
            @PathVariable("name") String name,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String email = customUserDetails.getUsername();
        chatRoomService.checkDuplicateRoom(name);
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "채팅룸 name=" + name + " 생성 성공",
                new ChatRoomDetailDto().createRoom(chatRoomService.createRoom(name, memberService.findByEmail(email)))
        );
    }

    // Admin 권한에서만 삭제 가능하도록 수정
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/chat/room/{roomId}")
    public ResponseEntity<CommonResponse> deleteRoom(@PathVariable("roomId") String roomId) {
        chatRoomService.deleteRoom(chatRoomService.findRoomByRoomId(roomId));
        return CommonResponse.responseMessage(
                HttpStatus.NO_CONTENT,
                "채팅룸 id=" + roomId + " 정상 삭제"
        );
    }

    // 채팅 방 입장
    // ChatRoomDetailDto 반환하도록 수정할 것.
    @GetMapping("/chat/room/enter/{roomId}")
    public ChatRoom roomDetail(@PathVariable("roomId") String roomId) {
        return chatRoomService.findRoomByRoomId(roomId);
    }
}
