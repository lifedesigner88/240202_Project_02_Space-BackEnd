package com.encore.space.domain.chat.dto;

import com.encore.space.domain.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDto {
    private String id;
    private String name;
    private String host;
    private int number; // 참여자 수

    public ChatRoomDetailDto createRoom(ChatRoom chatRoom) {
        ChatRoomDetailDto roomDetailResDto = new ChatRoomDetailDto();
        roomDetailResDto.setId(chatRoom.getRoomId());
        roomDetailResDto.setName(chatRoom.getRoomName());
        roomDetailResDto.setHost(chatRoom.getHost());
        roomDetailResDto.setNumber(0);
        return roomDetailResDto;
    }

}
