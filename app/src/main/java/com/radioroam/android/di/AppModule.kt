package com.radioroam.android.di

import android.content.Context
import android.telephony.TelephonyManager
import androidx.room.Room
import com.radioroam.android.BuildConfig
import com.radioroam.android.data.database.AppDatabase
import com.radioroam.android.data.datasource.RadioStationsRemoteDataSource
import com.radioroam.android.data.network.ApiEndpoints
import com.radioroam.android.data.network.ApiService
import com.radioroam.android.data.network.ApiServiceImpl
import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.usecase.AddToOrRemoveFromFavoritesUseCase
import com.radioroam.android.domain.usecase.GetFavoriteRadioStationsUseCase
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import com.radioroam.android.ui.viewmodel.FavoritesViewModel
import com.radioroam.android.ui.viewmodel.HomeViewModel
import com.radioroam.android.ui.viewmodel.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(OkHttp) {
            expectSuccess = true

            defaultRequest {
                url {
                    host = ApiEndpoints.API_HOST
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

    single {
        Room.databaseBuilder(
            context = androidContext().applicationContext,
            klass = AppDatabase::class.java,
            name = "radio_stations.db"
        ).build()
    }

    factory { androidContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager }
    single<ApiService> { ApiServiceImpl(get()) }
    factory { RadioStationsRemoteDataSource(get()) }
    factory { RadioStationRepository(get(), get()) }
    factory { GetRadioStationsUseCase(get(), get()) }
    factory { GetFavoriteRadioStationsUseCase(get()) }
    factory { AddToOrRemoveFromFavoritesUseCase(get()) }
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
}