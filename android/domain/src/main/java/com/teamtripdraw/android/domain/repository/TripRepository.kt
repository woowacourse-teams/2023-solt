package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle

interface TripRepository {
    suspend fun startTrip(): Result<Unit>
    fun getCurrentTripId(): Long
    fun deleteCurrentTripId()
    suspend fun getCurrentTripRoute(tripId: Long): Result<Route>
    suspend fun setTripTitle(tripId: Long, preSetTripTitle: PreSetTripTitle): Result<Unit>
}
