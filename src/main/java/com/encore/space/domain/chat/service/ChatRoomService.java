package com.encore.space.domain.chat.service;

import com.encore.space.domain.chat.domain.ChatRoom;
import com.encore.space.domain.chat.dto.ChatResDto;
import com.encore.space.domain.chat.dto.ChatRoomCreateDto;
import com.encore.space.domain.chat.dto.ChatRoomDetailDto;
import com.encore.space.domain.chat.dto.ChatRoomSearchDto;
import com.encore.space.domain.chat.repository.ChatRoomRepository;
import com.encore.space.domain.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createRoom(String name, Member member) {
        ChatRoomCreateDto dto = new ChatRoomCreateDto().create(name);
        ChatRoom chatRoom = ChatRoom.toChatRoom(name, dto.getRoomId(), member.getNickname());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findRoomByRoomId(String roomId) {
        return getRoomByRoomId(roomId);
    }

    public ChatRoom findRoomByRoomName(String roomName) {
        return getRoomByRoomName(roomName);
    }

    private ChatRoom getRoomByRoomId(String roomId) {
        Optional<ChatRoom> room = chatRoomRepository.findByRoomId(roomId);

        if (room.isPresent()) {
            return room.get();
        }
        throw new EntityNotFoundException("채팅룸을 찾을 수 없습니다.");
    }

    private ChatRoom getRoomByRoomName(String roomName) {
        Optional<ChatRoom> room = chatRoomRepository.findByRoomName(roomName);

        if (room.isPresent()) {
            return room.get();
        }
        throw new EntityNotFoundException("채팅룸을 찾을 수 없습니다.");
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public void deleteRoom(ChatRoom chatRoom) {
        log.info("제거된 방 {}", chatRoom.getRoomId());
        chatRoomRepository.delete(chatRoom);
    }

    public void checkDuplicateRoom(String name) {
        if (chatRoomRepository.findByRoomName(name).isPresent()) {
            throw new IllegalArgumentException("중복된 채팅룸이 존재합니다.");
        }
    }

    public List<ChatRoomDetailDto> findAll(ChatRoomSearchDto searchDto, Pageable pageable) {
        Specification<ChatRoom> spec = new Specification<ChatRoom>() {
            @Override
            public Predicate toPredicate(Root<ChatRoom> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (searchDto.getHost() != null) {
                    predicates.add(criteriaBuilder.like(root.get("host"), "%" + searchDto.getHost() + "%"));
                }
                if (searchDto.getRoomName() != null) {
                    predicates.add(criteriaBuilder.like(root.get("roomName"), "%" + searchDto.getRoomName() + "%"));
                }
                Predicate[] predicatesArr = new Predicate[predicates.size()];
                for (int i = 0; i < predicates.size(); i++) {
                    predicatesArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicatesArr);
                return predicate;
            }
        };
        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(spec, pageable);
        List<ChatRoom> chatRoomList = chatRooms.getContent();
        List<ChatRoomDetailDto> chatRoomDetailDtoList = chatRoomList.stream()
                .map(chatRoom -> ChatRoomDetailDto.builder()
                        .id(chatRoom.getRoomId())
                        .name(chatRoom.getRoomName())
                        .host(chatRoom.getHost())
                        .chatList(chatRoom.getChatList().stream().map(ChatResDto::convertToDto).toList())
                        .build())
                .collect(Collectors.toList());
        return chatRoomDetailDtoList;
    }


    public ChatRoomDetailDto findRoomIdChatList(String roomId) {
        return ChatRoomDetailDto.createRoom(findRoomByRoomId(roomId));
    }
}
