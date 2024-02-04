package com.encore.space.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Data
@Builder
@AllArgsConstructor
public class CommonResponse {
    private HttpStatus httpStatus;
    private String message;
    private Object result;

    public static ResponseEntity<CommonResponse> responseMassage(HttpStatus status, String message, Object object) {
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
    public static ResponseEntity<CommonResponse> responseMassage(HttpStatus status, String message){
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
