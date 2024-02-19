package com.encore.space.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatReqDto {
    private String roomId;  // 채팅방 ID
    private String sender;  // 보내는이
    private String message; // 메시지
}
