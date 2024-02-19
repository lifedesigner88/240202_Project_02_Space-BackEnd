package com.encore.space.domain.chat.repository;

import com.encore.space.domain.chat.domain.Chat;
import com.encore.space.domain.chat.domain.ChatRoom;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    /**
     * 주어진 시각인 sendAt 이후에 ChatRoom 인스턴스에서 발생한 모든 채팅 데이터를 List 형태로 반환한다.
     */
    List<Chat> findAllBySendAtAfterAndChatroom(LocalDateTime sendAt, ChatRoom chatroom);

    List<Chat> findAllById(Long id);
}
