package com.mobisigma.pizzabeer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
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

@RunWith(MockitoJUnitRunner::class)
class SearchBusinessUseCaseTest {
    @get:Rule
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repo: BusinessRepository

    private lateinit var useCase: SearchBusinessUseCase

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        useCase = SearchBusinessUseCase(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun searchBusinessUseCase_searchPizzaAndBeer_shouldSucceed() = runTest {
        val pizzaBusinesses = listOf(
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

            )
        )

        val beerBusinesses = listOf(
            BusinessEntity(
                id = "LSOS",
                name = "Always Brewing",
                imageUrl = "",
                rating = 4.5,
                reviewCount = 223
            )
        )

        val allBusinesses = (pizzaBusinesses + beerBusinesses).distinctBy { it.id }

        Mockito.`when`(repo.search("pizza", Location("92620"), 0)).thenReturn(pizzaBusinesses)
        Mockito.`when`(repo.search("beer", Location("92620"), 0)).thenReturn(beerBusinesses)

        val searchResult = useCase.searchPizzaAndBeer(Location("92620"))

        advanceUntilIdle()

        Assert.assertTrue(searchResult is SearchBusinessUseCase.SearchUiState.Success)
        assertEquals(allBusinesses.size, (searchResult as SearchBusinessUseCase.SearchUiState.Success).data.size)
    }
}