package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.TripStatus

interface TripRepository {
    suspend fun startTrip(): Result<Unit>
    fun getCurrentTripId(): Long
    suspend fun getCurrentTripRoute(tripId: Long): Result<Route>
    suspend fun setTripTitle(tripId: Long, name: String, status: TripStatus): Result<Unit>
}
