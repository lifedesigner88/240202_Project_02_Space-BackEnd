package com.encore.space.domain.chat.dto;

import com.encore.space.domain.chat.domain.MemberChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberChatRoomDto {
    private Long id;
    private String memberNickname;

    public MemberChatRoomDto(MemberChatRoom memberChatRoom) {
        this.id = memberChatRoom.getId();
        this.memberNickname = memberChatRoom.getMember().getNickname();
    }
}
