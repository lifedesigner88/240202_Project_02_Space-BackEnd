package com.encore.space.domain.schedule.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.schedule.dto.reqdto.CreateScheduleReqDto;
import com.encore.space.domain.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("schedule")
public class ScheduleController {


    private final ScheduleService scheduleService;
    public ScheduleController(@Autowired ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

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
