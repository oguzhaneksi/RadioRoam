package com.radioroam.android.data.network

import com.radioroam.android.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class ApiServiceImpl(
    private val ktor: HttpClient
): ApiService {

    override suspend fun getRadioStationsByCountry(
        isoCountryCode: String,
        limit: Int,
        offset: Int,
        hideBroken: Boolean
    ): HttpResponse = ktor.get(
        urlString = "${ApiEndpoints.API_HOST}${ApiEndpoints.STATIONS_BY_COUNTRY_CODE_EXACT.replace(ApiEndpoints.COUNTRY_CODE, isoCountryCode)}"
    ) {
        parameter("limit", limit)
        parameter("offset", offset)
        parameter("hidebroken", hideBroken)
    }

}