package com.encore.space.common.response;

import com.encore.space.domain.chat.dto.*;
import com.encore.space.domain.comment.dto.CommentChildrenListDto;
import com.encore.space.domain.comment.dto.CommentCreateDto;
import com.encore.space.domain.comment.dto.CommentResDto;
import com.encore.space.domain.comment.dto.CommentUpdateDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import com.encore.space.domain.post.dto.PostDetailResDto;
import com.encore.space.domain.post.dto.PostListDto;
import com.encore.space.domain.schedule.dto.resdto.CreateScheduleResDto;
import com.encore.space.domain.space.dto.resdto.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Data
@Builder
@AllArgsConstructor
@Schema(description = "반환 클래스")
public class CommonResponse {

    @Schema(description = "HttpStatus 타입")
    private HttpStatus httpStatus;

    @Schema(description = "반환 메시지")
    private String message;

    @Schema(description = "반환 오브젝트", anyOf = {
            String.class,
            MemberResDto.class,
            ChatResDto.class,
            ChatRoomCreateDto.class,
            ChatRoomDetailDto.class,
            ChatRoomSearchDto.class,
            ChatRoomSubsDto.class,
            MemberChatRoomDto.class,
            CommentChildrenListDto.class,
            CommentCreateDto.class,
            CommentResDto.class,
            CommentUpdateDto.class,
            PostDetailResDto.class,
            PostListDto.class,
            CreateScheduleResDto.class,
            CreateSpaceResDto.class,
            GetAllSpacesResDto.class,
            GetSpacesByEmailResDto.class,
            MembersBySpaceResDto.class,
            PostsBySpaceResDto.class,
            SchedulesBySpaceResDto.class
    })
    private Object result;

    public static ResponseEntity<CommonResponse> responseMessage(HttpStatus status, String message, Object object) {
        return new ResponseEntity<>(
                CommonResponse.builder()
                        .httpStatus(status)
                        .message(message)
                        .result(object)
                        .build()
                ,
                status
        );
    }
    public static ResponseEntity<CommonResponse> responseMessage(HttpStatus status, String message){
        return new ResponseEntity<>(
                CommonResponse.builder()
                        .httpStatus(status)
                        .message(message)
                        .build()
                ,
                status
        );
    }

}
