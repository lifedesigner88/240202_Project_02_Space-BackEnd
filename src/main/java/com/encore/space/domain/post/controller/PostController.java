package com.encore.space.domain.post.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.login.domain.CustomUserDetails;
import com.encore.space.domain.post.dto.PostCreateDto;
import com.encore.space.domain.post.dto.PostUpdateDto;
import com.encore.space.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequestMapping("/api/post")
@Tag(name = "게시판 관련 API")
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "게시글 작성",
            description = "게시글 작성"
    )
    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createPost(@ModelAttribute PostCreateDto postCreateDto,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails){
        postService.save(postCreateDto, userDetails.getUsername());
        return CommonResponse.responseMassage(
                HttpStatus.CREATED,
                "게시글이 성공적으로 작성되었습니다.");
    }

    @Operation(
            summary = "게시글 목록 조회",
            description = "게시글 목록"
    )
    @GetMapping("/list")
    public ResponseEntity<CommonResponse> getPostList(){
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "post list successfully loaded",
                postService.findAll());
    }

    @Operation(
            summary = "게시글 글 조회",
            description = "게시글 상세 조회"
    )
    @GetMapping("/detail/{post_id}")
    public ResponseEntity<CommonResponse> getPost(@PathVariable("post_id") Long id){
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "post successfully loaded",
                postService.getPost(id));
    }

    @Operation(
            summary = "게시글 수정",
            description = "게시글 수정"
    )
    @PostMapping("/update/{post_id}")
    public ResponseEntity<CommonResponse> postUpdate(@PathVariable("post_id") Long id,
                                                     @ModelAttribute PostUpdateDto postUpdateDto,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        postService.updatePost(id,postUpdateDto,userDetails.getUsername());
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "post successfully updated");
    }

    @Operation(
            summary = "게시글 삭제",
            description = "게시글 삭제"
    )
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<CommonResponse> delete (@PathVariable("postId") long id,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        postService.delete(id,userDetails.getUsername());
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "post successfully deleted");
    }
}
