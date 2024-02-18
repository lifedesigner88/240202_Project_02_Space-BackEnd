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


    @PostMapping("{spaceId}/create/{email}") // 레디스 고장
    public ResponseEntity<CommonResponse> createSchedule(
            @PathVariable String spaceId,
            @PathVariable String email, // 레디스 고장나서 임시로
            @RequestBody CreateScheduleReqDto dto) {

        return response(HttpStatus.CREATED, "테스트", scheduleService.createSchedule(spaceId,  email, dto));
    }

    public ResponseEntity<CommonResponse> response(HttpStatus httpStatus, String message, Object object) {
        return new ResponseEntity<>(new CommonResponse(httpStatus, message, object), httpStatus);
    }
}
