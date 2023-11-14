package com.radioroam.android.di

import com.radioroam.android.BuildConfig
import com.radioroam.android.data.network.ApiService
import com.radioroam.android.data.network.ApiServiceImpl
import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import com.radioroam.android.ui.viewmodel.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(OkHttp) {
            expectSuccess = true

            defaultRequest {
                url {
                    host = BuildConfig.API_HOST
                    url { protocol = URLProtocol.HTTPS }
                }
            }

            if (BuildConfig.DEBUG) {
                engine {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(
                            HttpLoggingInterceptor.Level.BODY
                        )
                    })
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    serializersModule = SerializersModule {
                        ignoreUnknownKeys = true
                    }
                })
            }
        }
    }
    single<ApiService> { ApiServiceImpl(get()) }
    factory { RadioStationRepository(get()) }
    factory { GetRadioStationsUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}