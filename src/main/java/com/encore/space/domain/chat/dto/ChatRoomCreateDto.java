package com.encore.space.domain.chat.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCreateDto {
    private String roomId;
    private String roomName;

    public ChatRoomCreateDto create(String roomName) {
        ChatRoomCreateDto chatRoom = new ChatRoomCreateDto();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
}
