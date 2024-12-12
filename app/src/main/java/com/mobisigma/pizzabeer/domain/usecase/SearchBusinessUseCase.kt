package com.mobisigma.pizzabeer.domain.usecase

import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SearchBusinessUseCase(private val businessRepository: BusinessRepository) {
    private lateinit var location: Location
    private val businessEntities = mutableListOf<BusinessEntity>()
    private var pizzaOffset = 0
    private var beerOffset = 0

    sealed class SearchUiState {
        class Success(val data: List<BusinessEntity>) : SearchUiState()
        data object Failure: SearchUiState()
    }

    suspend fun searchPizzaAndBeer(location: Location): SearchUiState {
        this.location = location

        businessEntities.clear()
        return search()
    }

    private suspend fun search(): SearchUiState {
        return withContext(Dispatchers.IO) {
            try {
                val pizzaBusinessesDeferred = async { businessRepository.search("pizza", location, pizzaOffset) }
                val beerBusinessesDeferred = async { businessRepository.search("beer", location, beerOffset) }

                val pizzaBusinesses = pizzaBusinessesDeferred.await()
                val beerBusinesses = beerBusinessesDeferred.await()

                pizzaOffset += pizzaBusinesses.size
                beerOffset += beerBusinesses.size

                val allBusinesses = (pizzaBusinesses + beerBusinesses).distinctBy { it.id }
                businessEntities.addAll(allBusinesses)
                return@withContext SearchUiState.Success(data = businessEntities.toList())
            } catch (t: Throwable) {
                return@withContext SearchUiState.Failure
            }
        }
    }

    suspend fun searchMore(): SearchUiState {
        return search()
    }
}