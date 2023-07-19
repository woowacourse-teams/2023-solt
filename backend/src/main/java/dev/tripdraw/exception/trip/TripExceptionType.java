package dev.tripdraw.exception.trip;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum TripExceptionType implements ExceptionType {
    TRIP_NOT_FOUND(NOT_FOUND, "존재하지 않는 여행입니다."),
    NOT_AUTHORIZED(FORBIDDEN, "해당 여행에 대한 접근 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    TripExceptionType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}