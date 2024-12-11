package com.mobisigma.pizzabeer.domain.usecase

import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SearchBusinessUseCase constructor(private val businessRepository: BusinessRepository) {
    sealed class SearchUiState {
        data object Loading: SearchUiState()
        class Success(val data: List<BusinessEntity>) : SearchUiState()
        data object Failure: SearchUiState()
    }

    suspend fun searchPizzaAndBeer(location: String): SearchUiState {
        return withContext(Dispatchers.IO) {
            try {
                val pizzaBusinessesDeferred = async { businessRepository.search("pizza", location) }
                val beerBusinessesDeferred = async { businessRepository.search("beer", location) }

                val pizzaBusinesses = pizzaBusinessesDeferred.await()
                val beerBusinesses = beerBusinessesDeferred.await()

                val allBusinesses = (pizzaBusinesses + beerBusinesses).distinctBy { it.id }

                return@withContext SearchUiState.Success(data = allBusinesses)
            } catch (t: Throwable) {
                return@withContext SearchUiState.Failure
            }
        }
    }
}