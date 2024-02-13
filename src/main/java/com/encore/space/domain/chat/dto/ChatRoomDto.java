package com.encore.space.domain.chat.dto;

import java.util.HashMap;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDto {
    private String roomId;  // 채팅방 ID
    private String roomName;    // 채팅방명
    private int userCount;  // 채팅방 인원수
    private int maxUserCount;   // 채팅방 최대 인원수

    private String roomPassword;    // 채팅방 password, 채팅방 삭제 시에도 활용.
    private String isSecretRoom;    // 채팅방 잠금 여부.(Y/N)

    private HashMap<String, String> userList;
}
