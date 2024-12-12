package com.mobisigma.pizzabeer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import com.mobisigma.pizzabeer.ui.viewmodel.SearchBusinessViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchBusinessViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockSearchBusinessUseCase: SearchBusinessUseCase

    private lateinit var viewModel: SearchBusinessViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = SearchBusinessViewModel(mockSearchBusinessUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun searchBusinessViewModel_businessesSuccessfullyLoaded_businessesUpdated() = runTest {

        val allBusinesses = listOf(
            BusinessEntity(
                id = "ABCD",
                name = "First Class Pizza",
                imageUrl = "",
                rating = 4.5,
                reviewCount = 223
            ),
            BusinessEntity(
                id = "DSEF",
                name = "Papa Johns",
                imageUrl = "",
                rating = 4.5,
                reviewCount = 223

            ),
            BusinessEntity(
                id = "LSOS",
                name = "Always Brewing",
                imageUrl = "",
                rating = 4.5,
                reviewCount = 223
            )
        )

        val searchResultState = SearchBusinessUseCase.SearchUiState.Success(allBusinesses)

        Mockito.`when`(mockSearchBusinessUseCase.searchPizzaAndBeer(Location("92620"))).thenReturn(searchResultState)

        viewModel.search(Location("92620"))

        advanceUntilIdle()

        val searchResultUiState = viewModel.searchResult.value
        Assert.assertNotNull(searchResultUiState)
        Assert.assertTrue(searchResultUiState is SearchBusinessUseCase.SearchUiState.Success)
        assertEquals(allBusinesses.size, (searchResultUiState as SearchBusinessUseCase.SearchUiState.Success).data.size)
    }
}