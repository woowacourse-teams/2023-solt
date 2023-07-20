package dev.tripdraw.presentation.controller;

import dev.tripdraw.application.TripService;
import dev.tripdraw.config.swagger.SwaggerLoginRequired;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import dev.tripdraw.dto.response.TripCreateResponse;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@RequiredArgsConstructor
@RestController
public class TripController {

    private final TripService tripService;

    @SwaggerLoginRequired
    @Operation(summary = "여행 생성 API", description = "현재 로그인한 사용자의 여행을 생성합니다.", tags = {"여행", "로그인"})
    @PostMapping("/trips")
    public ResponseEntity<TripCreateResponse> create(@Auth LoginUser loginUser) {

        TripCreateResponse response = tripService.create(loginUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SwaggerLoginRequired
    @Operation(summary = "위치 정보 저장 API", description = "현재 진행 중인 여행의 경로에 위치 정보를 저장합니다..", tags = {"여행", "위치", "로그인"})
    @PostMapping("/points")
    public ResponseEntity<PointCreateResponse> addPoint(
            @Auth LoginUser loginUser,
            @RequestBody PointCreateRequest pointCreateRequest
    ) {
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
