package com.encore.space.domain.chat.dto;

import com.encore.space.domain.chat.domain.ChatRoom;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDto {
    private String id;
    private String name;
    private String host;
    private List<ChatResDto> chatList;
    private int number; // 참여자 수

    public static ChatRoomDetailDto createRoom(ChatRoom chatRoom) {
        ChatRoomDetailDto roomDetailResDto = new ChatRoomDetailDto();
        roomDetailResDto.setId(chatRoom.getRoomId());
        roomDetailResDto.setName(chatRoom.getRoomName());
        roomDetailResDto.setHost(chatRoom.getHost());
        roomDetailResDto.setNumber(0);
        roomDetailResDto.setChatList(
                chatRoom.getChatList().stream().map(ChatResDto::convertToDto).toList()
        );
        return roomDetailResDto;
    }

}
