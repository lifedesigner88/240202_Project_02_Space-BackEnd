package com.encore.space.domain.space.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.space.domain.SpaceType;
import com.encore.space.domain.space.dto.reqdto.CreateSpaceReqDto;
import com.encore.space.domain.space.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원관련 API")
@RestController
@RequestMapping("space")
public class SpaceController {


    private final SpaceService spaceService;

    public SpaceController(@Autowired SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    //    Create

    @Operation(
            summary = "MY Space 생성",
            description = "MY Space 생성 SPACE 생성 끼리는 같음 함수 공유."
    )
    @PostMapping("create/my")
    public ResponseEntity<CommonResponse> createMySpace(@RequestBody CreateSpaceReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "개인 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.MY, dto));
    }

    @Operation(
            summary = "TEAM Space 생성",
            description = "TEAM Space 생성 SPACE 생성 끼리는 같음 함수 공유."
    )
    @PostMapping("create/team")
    public ResponseEntity<CommonResponse> createTeamSpace(@RequestBody CreateSpaceReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "팀 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.TEAM, dto));
    }

    @Operation(
            summary = "GROUP Space 생성",
            description = "GROUP Space 생성 SPACE 생성 끼리는 같음 함수 공유."
    )
    @PostMapping("create/group")
    public ResponseEntity<CommonResponse> createGroupSpace(@RequestBody CreateSpaceReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "그룹 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.GROUP, dto));
    }


    //    Read
    @Operation(
            summary = "GatAllSpaces 생성된 모든 스페이스 불러오기",
            description = "참여여부와 관련없이 모든 스페이스를 가지고옴"
    )
    @GetMapping("spaces")
    public ResponseEntity<CommonResponse> getAllSpaces() {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "모든 생성된 스페이스 정보를 조회하였습니다.",
                spaceService.getAllSpaces());
    }

    @Operation(
            summary = "내가 가입되어 있는 스페이스 불러오기",
            description = "내가 참여중인 스페이스만 불러옴 추후 필터링을 통해 화면에 뿌려줌"
    )
    @GetMapping("my")
    public ResponseEntity<CommonResponse> getSpacesByEmail() {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "로그인한 회원이 속해있는 스페이스 정보를 조회하였습니다.",
                spaceService.getSpacesByEamil());
    }


    @Operation(
            summary = "특정 스페이스에 참여중인 회원목록",
            description = "스페이스 아이디로 맴버들 조회해옴"
    )
    @GetMapping("{spaceId}/members")
    public ResponseEntity<CommonResponse> getMembersBySapceId(@PathVariable Long spaceId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "스페이스의 맴버 정보를 조회하였습니다",
                spaceService.getMembersBySpaceId(spaceId));
    }

    @Operation(
            summary = "특정 스페이스에 등록된 일정 목록",
            description = "스페이스 아이디로 일정 조회"
    )
    @GetMapping("{spaceId}/schedules")
    public ResponseEntity<CommonResponse> getSchedulesBySapceId(@PathVariable Long spaceId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "스페이스의 일정정보를 모두 조회하였습니다",
                spaceService.getSchedulesBySpaceId(spaceId));
    }

    @Operation(
            summary = "특정 스페이스에 등록된 게시글 목록",
            description = "스페이스 아이디로 게시글 조회"
    )
    @GetMapping("{spaceId}/posts")
    public ResponseEntity<CommonResponse> getPostsBySapceId(@PathVariable Long spaceId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "스페이스의 글을 모두 조회하였습니다",
                spaceService.getPostsBySpaceId(spaceId));
    }

}
