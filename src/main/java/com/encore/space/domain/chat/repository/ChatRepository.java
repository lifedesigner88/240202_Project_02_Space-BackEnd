package com.encore.space.domain.chat.repository;

import com.encore.space.domain.chat.dto.ChatRoomDto;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ChatRepository {
    // service와 repository로 분리해야 함

    private Map<String, ChatRoomDto> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    /**
     * 전체 채팅방 조회
     */
    public List<ChatRoomDto> findAllChatRoom() {
        // 채팅방 생성 순서를 최근순으로 반환
        List<ChatRoomDto> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    /**
     * roomId를 기준으로 채팅방 찾기
     */
    public ChatRoomDto findChatRoomById(String roomId) {
        return chatRoomMap.get(roomId);
    }

    /**
     * roomName으로 채팅방 생성하기
     */
    public ChatRoomDto createChatRoom(String roomName, String roomPassword, String isSecretRoom, int maxUserCount) {
        ChatRoomDto chatRoom = ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .roomPassword(roomPassword)
                .isSecretRoom(isSecretRoom)
                .userList(new HashMap<String, String>())    // userUUID, userName
                .userCount(0)
                .maxUserCount(maxUserCount)
                .build();
        // map에 채팅룸 아이디와 만들어진 채팅룸을 저장
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    // 채팅방 인원 추가
    public void plusUserCount(String roomId) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
    }

    // 채팅방 인원 감소
    public void minusUserCount(String roomId) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount() - 1);
    }

    // maxUserCount에 따른 채팅방 입장 여부
    public boolean checkRoomUserCount(String roomId) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        log.info("참여인원 확인 [{}, {}]", chatRoom.getUserCount(), chatRoom.getMaxUserCount());
        if (chatRoom.getUserCount() + 1 > chatRoom.getMaxUserCount()) {
            return false;
        }
        return true;
    }

    // 채팅방 유저 리스트에 유저 추가
    public String addUser(String roomId, String userName) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        chatRoom.getUserList().put(userUUID, userName);
        return userUUID;
    }

    /**
     * 채팅방의 유저 이름이 중복되면 랜덤한 숫자를 붙여서 중복되지 않은 userName을 반환한다.
     * @param roomId
     * @param userName
     * @return 중복이 없으면 tmp. 중복될 경우 중복을 해결한 이름.
     */
    public String isDuplicateName(String roomId, String userName) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        String tmp = userName;
        while (chatRoom.getUserList().containsValue(tmp)) {
            tmp = userName + new Random().nextInt(100);
        }
        return tmp;
    }

    // 채팅방 유저 리스트 삭제
    public void deleteUser(String roomId, String userUUID) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        chatRoom.getUserList().remove(userUUID);
    }

    // 채팅방 userName 조회
    public String getUserName(String roomId, String userUUID) {
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);
        return chatRoom.getUserList().get(userUUID);
    }

    // 채팅방 전체 userlist 조회
    public ArrayList<String> getUserList(String roomId) {
        ArrayList<String> list = new ArrayList<>();
        ChatRoomDto chatRoom = chatRoomMap.get(roomId);

        // hashmap을 for문을 돌린 후 value값만 뽑아내서 list에 저장 후 return
        chatRoom.getUserList().forEach((key, value) -> list.add(value));
        return list;
    }

    // 채팅방 비밀번호가 맞는지 확인
    public boolean confirmPassword(String roomId, String roomPassword) {
        return roomPassword.equals(chatRoomMap.get(roomId).getRoomPassword());
    }

    /**
     * 채팅방 삭제
     */
    public void deleteChatRoom(String roomId) {
        try {
            // 채팅방 삭제
            chatRoomMap.remove(roomId);

            log.info("삭제 완료 roomId : {}", roomId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
