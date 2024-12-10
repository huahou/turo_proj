package com.mobisigma.pizzabeer.domain.usecase

import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.domain.repository.BusinessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchBusinessUseCase constructor(val businessRepository: BusinessRepository) {
    sealed class SearchUiState {
        data object Loading: SearchUiState()
        class Success(val data: List<BusinessEntity>) : SearchUiState()
        data object Failure: SearchUiState()
    }

    suspend fun search(keyword: String, location: String): SearchUiState {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext SearchUiState.Success(data = businessRepository.search(keyword, location))
            } catch (t: Throwable) {
                return@withContext SearchUiState.Failure
            }
        }
    }
}