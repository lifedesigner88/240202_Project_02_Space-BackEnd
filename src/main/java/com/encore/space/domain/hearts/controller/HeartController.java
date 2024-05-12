package com.encore.space.domain.hearts.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.hearts.dto.HeartReqDto;
import com.encore.space.domain.hearts.service.HeartService;
import com.encore.space.domain.login.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/heart")
@Tag(name = "좋아요 관련 API")
public class HeartController {
    private final HeartService heartService;
    @Autowired
    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    @Operation(
            summary = "좋아요 추가",
            description = "게시물이나 댓글에 좋아요 붙일때 사용하는 API, 로그인한 사용자 한해서"
    )
    @PostMapping("/add")
    public ResponseEntity<CommonResponse> giveHeart(@RequestBody HeartReqDto heartReqDto,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        heartService.giveHeart(heartReqDto,userDetails.getUsername());
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "좋아요!");
    }

    @Operation(
            summary = "좋아요 취소",
            description = "게시물이나 댓글에 단 좋아요 취소할때, 로그인한 사용자 한해서"
    )
    @PostMapping("/cancel")
    public ResponseEntity<CommonResponse> takeHeart(@RequestBody HeartReqDto heartReqDto,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        heartService.cancelHeart(heartReqDto, userDetails.getUsername());
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "좋아요가 취소되었습니다.");
    }

    @Operation(
            summary = "게시글 좋아요 갯수",
            description = "게시글에 붙은 좋아요 갯수"
    )
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse> postHearts(@PathVariable("postId") Long id){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "here are post hearts",
                heartService.postHearts(id));
    }

    @Operation(
            summary = "댓글 좋아요 갯수",
            description = "댓글에 붙은 좋아요 갯수"
    )
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponse> commentHearts(@PathVariable("commentId") Long id){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "here are comment hearts",
                heartService.commentHearts(id));
    }
}
