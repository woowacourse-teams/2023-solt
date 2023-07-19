package dev.tripdraw.presentation.controller;

import dev.tripdraw.application.TripService;
import dev.tripdraw.dto.response.TripCreationResponse;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import dev.tripdraw.presentation.member.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripCreationResponse> create() {
        TripCreationResponse tripCreationResponse = tripService.create();
        return null;
    }

    @PostMapping("/points")
    public ResponseEntity<PointCreateResponse> addPoint(
            @Auth LoginUser loginUser,
            @RequestBody PointCreateRequest pointCreateRequest
    ) {
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
