package com.encore.space.domain.chat.dto;

import com.encore.space.domain.chat.domain.Chat;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResDto {
    private Long id;
    private String messageType;
    private String roomId;
    private String sender;
    private String message;
    private String time;

    public static ChatResDto convertToDto(Chat chat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        String formatedNow = chat.getSendAt().format(formatter);
        return new ChatResDto(
                chat.getId(),
                chat.getMessageType().toString(),
                chat.getChatroom().getRoomId(),
                chat.getSender(),
                chat.getMessage(),
                formatedNow);
    }
}
