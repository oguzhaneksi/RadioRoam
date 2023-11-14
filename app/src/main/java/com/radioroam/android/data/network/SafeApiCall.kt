package com.radioroam.android.data.network

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

internal suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(data = apiCall.invoke())
    } catch (e: RedirectResponseException) { // 3xx errors
//        val networkError = getError(responseContent = e.response.bodyAsChannel())

        NetworkResult.Error(
            errorCode = /*networkError.code ?: */e.response.status.value.toString(),
            errorMessage = /*networkError.message ?: */e.message
        )
    } catch (e: ClientRequestException) { // 4xx errors
//        val networkError = getError(responseContent = e.response.bodyAsChannel())

        NetworkResult.Error(
            errorCode = /*networkError.code ?: */e.response.status.value.toString(),
            errorMessage = /*networkError.message ?: */e.message
        )
    } catch (e: ServerResponseException) { // 5xx errors
//        val networkError = getError(responseContent = e.response.bodyAsChannel())

        NetworkResult.Error(
            errorCode = /*networkError.code ?: */e.response.status.value.toString(),
            errorMessage = /*networkError.message ?: */e.message
        )
    } catch (e: Exception) {
        NetworkResult.Error(
            errorCode = "0",
            errorMessage = e.message ?: "An unknown error occurred"
        )
    }
}