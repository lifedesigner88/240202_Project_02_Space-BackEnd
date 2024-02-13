package com.encore.space.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    private String roomId;  // 채팅룸 ID
    private String sender;  // 채팅 송신자
    private String message; // 메시지
    private String createdAt;   // 메시지 발송 시간
    private MessageType type;   // 메시지 타입

    private String fileUploadUrl; // 파일 업로드 url
    private String fileName;    // 파일명
    private String fileUri; // 파일 경로

    /**
     * 메시지 타입: 입장, 채팅, 퇴장
     * ENTER와 LEAVE는 입장/퇴장 이벤트 처리가 실행되고,
     * TALK는 메시지 내용이 해당 채팅방을 subscribe하고 있는 모든 클라이언트에게 전달된다.
     */
    public enum MessageType {
        ENTER,
        TALK,
        LEAVE
    }
}
