package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.model.mapper.toDomainRoute
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.repository.TripRepository

class TripRepositoryImpl(
    private val remoteTripDataSource: TripDataSource.Remote,
    private val localTripDataSource: TripDataSource.Local
) :
    TripRepository {
    override suspend fun startTrip(): Result<Unit> =
        remoteTripDataSource.startTrip().onSuccess { tripId ->
            localTripDataSource.setCurrentTripId(tripId)
        }.map { }

    override fun getCurrentTripId(): Long =
        localTripDataSource.getCurrentTripId()

    override suspend fun getCurrentTripRoute(tripId: Long): Result<Route> =
        remoteTripDataSource.getTripInfo(tripId).map { dataTrip ->
            dataTrip.toDomainRoute()
        }
}