package com.encore.space.domain.space.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.space.domain.SpaceType;
import com.encore.space.domain.space.dto.reqdto.CreateSpaceReqDto;
import com.encore.space.domain.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("space")
public class SpaceController {


    private final SpaceService spaceService;

    public SpaceController(@Autowired SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    //    Create
    @PostMapping("create/my")
    public ResponseEntity<CommonResponse> createMySpace(@RequestBody CreateSpaceReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "개인 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.MY, dto));
    }

    @PostMapping("create/team")
    public ResponseEntity<CommonResponse> createTeamSpace(@RequestBody CreateSpaceReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "팀 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.TEAM, dto));
    }

    @PostMapping("create/group")
    public ResponseEntity<CommonResponse> createGroupSpace(@RequestBody CreateSpaceReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "그룹 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.GROUP, dto));
    }


    //    Read
    @GetMapping("spaces")
    public ResponseEntity<CommonResponse> getAllSpaces() {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "모든 생성된 스페이스 정보를 조회하였습니다.",
                spaceService.getAllSpaces());
    }

    @GetMapping("my")
    public ResponseEntity<CommonResponse> getSpacesByEmail() {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "로그인한 회원이 속해있는 스페이스 정보를 조회하였습니다.",
                spaceService.getSpacesByEamil());
    }

    @GetMapping("{spaceId}/members")
    public ResponseEntity<CommonResponse> getMembersBySapceId(@PathVariable Long spaceId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "스페이스의 맴버 정보를 조회하였습니다",
                spaceService.getMembersBySpaceId(spaceId));
    }

    @GetMapping("{spaceId}/schedules")
    public ResponseEntity<CommonResponse> getSchedulesBySapceId(@PathVariable Long spaceId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "스페이스의 일정정보를 모두 조회하였습니다",
                spaceService.getSchedulesBySpaceId(spaceId));
    }

    @GetMapping("{spaceId}/posts")
    public ResponseEntity<CommonResponse> getPostsBySapceId(@PathVariable Long spaceId) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "스페이스의 글을 모두 조회하였습니다",
                spaceService.getPostsBySpaceId(spaceId));
    }

}
