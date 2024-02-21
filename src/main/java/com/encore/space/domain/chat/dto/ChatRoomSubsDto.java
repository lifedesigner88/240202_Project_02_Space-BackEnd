package com.encore.space.domain.chat.dto;

import com.encore.space.domain.chat.domain.MemberChatRoom;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSubsDto {
    private List<MemberChatRoom> currentSubscriber;
}
