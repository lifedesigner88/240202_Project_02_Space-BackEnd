package com.encore.space.domain.space.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.space.domain.SpaceType;
import com.encore.space.domain.space.dto.reqdto.CreateSpaceReqDto;
import com.encore.space.domain.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("space")
public class SpaceController {


    private SpaceService spaceService;

    public SpaceController(@Autowired SpaceService spaceService) {
        this.spaceService = spaceService;
    }

//    Create
    @PostMapping("create/my")
    public ResponseEntity<CommonResponse> createMySpace(@RequestBody CreateSpaceReqDto dto) {
        return response(
                HttpStatus.CREATED, "개인 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.MY, dto));
    }

    @PostMapping("create/team")
    public ResponseEntity<CommonResponse> createTeamSpace(@RequestBody CreateSpaceReqDto dto) {
        return response(
                HttpStatus.CREATED, "팀 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.TEAM, dto));
    }

    @PostMapping("create/group")
    public ResponseEntity<CommonResponse> createGroupSpace(@RequestBody CreateSpaceReqDto dto) {
        return response(
                HttpStatus.CREATED, "그룹 스페이스가 생성되었습니다",
                spaceService.createSpaceWithMembers(SpaceType.GROUP, dto));
    }


//    리스폰 함수 공통화
    public ResponseEntity<CommonResponse> response(HttpStatus httpStatus, String message, Object object) {
        return new ResponseEntity<>(new CommonResponse(httpStatus, message, object), httpStatus);
    }

}
