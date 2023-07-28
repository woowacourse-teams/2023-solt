package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TripResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "경로")
        List<PointResponse> routes,

        @Schema(description = "여행 상태", example = "ONGOING")
        TripStatus status
) {

    public static TripResponse from(Trip trip) {
        return new TripResponse(trip.id(), trip.nameValue(), generateRoutes(trip), trip.status());
    }

    private static List<PointResponse> generateRoutes(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}
