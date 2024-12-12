package com.mobisigma.pizzabeer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mobisigma.pizzabeer.data.model.BusinessEntitiesResponse
import com.mobisigma.pizzabeer.data.model.BusinessEntityDto
import com.mobisigma.pizzabeer.data.repository.BusinessRepositoryImpl
import com.mobisigma.pizzabeer.data.source.remote.YelpRemoteApi
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class BusinessRepositoryTest {
    @get:Rule
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var yelpApi: YelpRemoteApi

    private lateinit var repo: BusinessRepository


    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        repo = BusinessRepositoryImpl(yelpApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun businessRepository_search_shouldSucceed() = runTest {
        val businesses = listOf(
            BusinessEntityDto(
                id = "ABCD",
                name = "First Class Pizza",
                imageUrl = "",
                rating = 4.5,
                reviewCount = 223
            ),
            BusinessEntityDto(
                id = "DSEF",
                name = "Papa Johns",
                imageUrl = "",
                rating = 4.5,
                reviewCount = 223

            )
        )

        val mockResponse = Response.success(BusinessEntitiesResponse(businesses, 66))

        Mockito.`when`(yelpApi.searchBusiness("pizza", "92620", null, null, 0)).thenReturn(mockResponse)

        val searchResult = repo.search("pizza", Location("92620"), 0)

        advanceUntilIdle()

        Assert.assertEquals(businesses.size, searchResult.size)
    }


}