package com.radioroam.android.domain

import com.radioroam.android.data.repository.RadioStationRepository
import com.radioroam.android.domain.usecase.GetRadioStationsUseCase
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetRadioStationsUseCaseTest {

    private lateinit var useCase: GetRadioStationsUseCase

    @Mock
    private lateinit var repository: RadioStationRepository

//    @Before
//    fun setUp() {
//        useCase = GetRadioStationsUseCase(repository)
//    }

//    @Test
//    fun `execute returns success state when repository fetches data successfully`() = runTest {
//        val radioStations = mockRadioStationDtoItems
//        `when`(repository.getRadioStationsByCountry("US"))
//            .thenReturn(NetworkResult.Success(radioStations))
//
//        val result = useCase.execute()
//
//        assertTrue(result is RadioStationsUiState.Success)
//        assertEquals((result as RadioStationsUiState.Success).data, radioStations.map { it.map() })
//    }
//
//    @Test
//    fun `execute returns error state when repository fetch fails`() = runTest {
//        `when`(repository.getRadioStationsByCountry("US"))
//            .thenReturn(NetworkResult.Error("","Network error"))
//
//        val result = useCase.execute()
//
//        assertTrue(result is RadioStationsUiState.Error)
//    }

}