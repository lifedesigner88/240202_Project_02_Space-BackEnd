package com.encore.space.domain.chat.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.chat.dto.ChatRoomDetailDto;
import com.encore.space.domain.chat.dto.ChatRoomSearchDto;
import com.encore.space.domain.chat.service.ChatRoomService;
import com.encore.space.domain.login.domain.CustomUserDetails;
import com.encore.space.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅룸 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @Operation(
            summary = "전체 채팅룸 검색",
            description = "모든 채팅룸을 검색"
    )
    @GetMapping("chat/rooms")
    public ResponseEntity<CommonResponse> findChatRooms(@AuthenticationPrincipal CustomUserDetails customUserDetails, ChatRoomSearchDto chatRoomSearchDto, Pageable pageable) {
        List<ChatRoomDetailDto> chatRoomDetailDtos = chatRoomService.findAll(chatRoomSearchDto, pageable);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                memberService.findByEmail(customUserDetails.getUsername()).getNickname(),
                chatRoomDetailDtos
        );
    }

    @Operation(
            summary = "채팅룸 생성",
            description = "특정 name의 채팅룸을 생성"
    )
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

    @Operation(
            summary = "채팅룸 삭제",
            description = "특정 roomId에 해당되는 채팅룸을 삭제"
    )
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/chat/room/{roomId}")
    public ResponseEntity<CommonResponse> deleteRoom(@PathVariable("roomId") String roomId) {
        chatRoomService.deleteRoom(chatRoomService.findRoomByRoomId(roomId));
        return CommonResponse.responseMessage(
                HttpStatus.NO_CONTENT,
                "채팅룸 id=" + roomId + " 정상 삭제"
        );
    }

    @Operation(
            summary = "채팅룸 상세 정보 조회",
            description = "특정 roomId에 해당되는 채팅룸의 상세 정보를 제공"
    )
    @GetMapping("/chat/room/enter/{roomId}")
    public ResponseEntity<CommonResponse> roomDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("roomId") String roomId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                memberService.findByEmail(customUserDetails.getUsername()).getNickname(),
                chatRoomService.findRoomIdChatList(roomId)
        );
    }

}
