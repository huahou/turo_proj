package com.mobisigma.pizzabeer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBusinessViewModel @Inject constructor(private val searchBusinessUseCase: SearchBusinessUseCase): ViewModel() {
    private val _searchResult: MutableStateFlow<SearchBusinessUseCase.SearchUiState> = MutableStateFlow(SearchBusinessUseCase.SearchUiState.InitState)
    val searchResult: StateFlow<SearchBusinessUseCase.SearchUiState> = _searchResult

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun search(location: Location? = null) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val resultState = searchBusinessUseCase.searchPizzaAndBeer(location)
                _searchResult.value = resultState
            }catch (t: Throwable) {
                _searchResult.value = SearchBusinessUseCase.SearchUiState.Failure
            }finally {
                _isLoading.value = false
            }
        }
    }
}