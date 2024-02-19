package com.encore.space.domain.comment.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.comment.dto.CommentCreateDto;
import com.encore.space.domain.comment.dto.CommentUpdateDto;
import com.encore.space.domain.comment.service.CommentService;
import com.encore.space.domain.login.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@Slf4j
@RequestMapping("/api/comment")
@Tag(name = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @Operation(
            summary = "댓글 등록",
            description = "로그인 된 사용자에 한해서 댓글 작성"
    )
    @PostMapping("/create/{postId}")
    public ResponseEntity<CommonResponse> makeComment(@PathVariable("postId") Long id,
                                                      @RequestBody CommentCreateDto commentCreateDto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        commentService.save(commentCreateDto, id,userDetails.getUsername());
        return CommonResponse.responseMassage(
                HttpStatus.CREATED,
                "comment successfully created");
    }

    //로그인 하고나서만 되는데..
    @Operation(
            summary = "댓글 목록 조회",
            description = "댓글 목록"
    )
    @GetMapping("/list/{postId}")
    public ResponseEntity<CommonResponse> getCommentList(@PathVariable("postId") Long postId){
       return CommonResponse.responseMassage(
               HttpStatus.OK,
               "comment list successfully loaded",
               commentService.findAll(postId));
    }

    @Operation(
            summary = "대댓글 목록 조회",
            description = "대댓글 목록"
    )
    @GetMapping("/list/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> getChildrenComments(@PathVariable("postId") Long postId,
                                                              @PathVariable("commentId") Long commentId ){
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "Children comment list successfully loaded",
                commentService.findChildrenComments(postId, commentId));
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글, 대댓글 수정"
    )
    @PostMapping("/update/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("commentId") Long id,
                                                        @RequestBody CommentUpdateDto commentUpdateDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        commentService.update(id, commentUpdateDto, userDetails.getUsername());
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "comment successfully updated");
    }

    @Operation(
            summary = "댓글 삭제",
            description = "댓글, 대댓글 삭제"
    )
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("commentId") Long commentId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        commentService.delete(commentId,userDetails.getUsername());
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "comment successfully deleted");
    }
}
