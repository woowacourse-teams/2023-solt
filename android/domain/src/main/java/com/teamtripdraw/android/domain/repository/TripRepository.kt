package com.teamtripdraw.android.domain.repository

interface TripRepository {
    suspend fun startTrip(): Result<Unit>

    fun getStoredTripId(): Long
}