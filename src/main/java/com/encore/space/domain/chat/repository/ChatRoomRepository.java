package com.encore.space.domain.chat.repository;

import com.encore.space.domain.chat.domain.ChatRoom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByRoomId(String roomId);
    Optional<ChatRoom> findByRoomName(String roomName);
    Page<ChatRoom> findAll(Specification<ChatRoom> specification, Pageable pageable);
}
