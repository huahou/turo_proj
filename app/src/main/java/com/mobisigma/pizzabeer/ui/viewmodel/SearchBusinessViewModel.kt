package com.mobisigma.pizzabeer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class SearchBusinessViewModel constructor(private val searchBusinessUseCase: SearchBusinessUseCase): ViewModel() {
    private val _searchResult: MutableLiveData<SearchBusinessUseCase.SearchUiState> = MutableLiveData()
    val searchResult: LiveData<SearchBusinessUseCase.SearchUiState> = _searchResult

    fun search(location: String) {
        _searchResult.postValue(SearchBusinessUseCase.SearchUiState.Loading)
        viewModelScope.launch {
            val encoded = searchBusinessUseCase.searchPizzaAndBeer(location)
            _searchResult.postValue(encoded)
        }
    }

}