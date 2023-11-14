package com.radioroam.android.data.network

import io.ktor.client.statement.HttpResponse

interface ApiService {

    suspend fun getRadioStationsByCountry(
        isoCountryCode: String,
        limit: Int,
        offset: Int,
        hideBroken: Boolean
    ): HttpResponse

}