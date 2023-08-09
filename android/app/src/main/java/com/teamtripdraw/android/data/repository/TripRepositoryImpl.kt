package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.data.model.mapper.toDomainRoute
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.TripRepository

class TripRepositoryImpl(
    private val remoteTripDataSource: TripDataSource.Remote,
    private val localTripDataSource: TripDataSource.Local,
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

    override suspend fun getTrip(tripId: Long): Result<Trip> =
        remoteTripDataSource.getTripInfo(tripId).map { it.toDomain() }

    override suspend fun setTripTitle(
        tripId: Long,
        preSetTripTitle: PreSetTripTitle,
    ): Result<Unit> =
        remoteTripDataSource.setTripTitle(tripId, preSetTripTitle.toData())

    override suspend fun getAllTrips(): Result<List<PreviewTrip>> =
        remoteTripDataSource.getAllTrips().map { trips -> trips.map { it.toDomain() } }
}
