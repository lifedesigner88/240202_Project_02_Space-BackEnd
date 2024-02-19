package com.encore.space.common.response;

import com.encore.space.domain.member.dto.resdto.MemberResDto;
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
            MemberResDto.class
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
