package com.encore.space.domain.schedule.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.schedule.dto.reqdto.CreateScheduleReqDto;
import com.encore.space.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "스케쥴 관련 API")
@RequestMapping("schedule")
public class ScheduleController {


    private final ScheduleService scheduleService;
    public ScheduleController(@Autowired ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(
            summary = "새로운 일정 생성",
            description = "새로운 일정 만들기"
    )
    @PostMapping("{spaceId}/create")
    public ResponseEntity<CommonResponse> createSchedule(
            @PathVariable String spaceId,
            @RequestBody CreateScheduleReqDto dto) {
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "스페이스에 일정이 생성되었습니다",
                scheduleService.createSchedule(spaceId, dto)

        );
    }
}
