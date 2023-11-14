package com.radioroam.android.data.network

sealed class NetworkResult<T>(
    val data: T? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(errorCode: String, errorMessage: String?) :
        NetworkResult<T>(errorCode = errorCode, errorMessage = errorMessage)
}
