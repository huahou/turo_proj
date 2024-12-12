package com.mobisigma.pizzabeer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobisigma.pizzabeer.domain.model.Location
import com.mobisigma.pizzabeer.domain.usecase.SearchBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBusinessViewModel @Inject constructor(private val searchBusinessUseCase: SearchBusinessUseCase): ViewModel() {
    private val _searchResult: MutableLiveData<SearchBusinessUseCase.SearchUiState> = MutableLiveData()
    val searchResult: LiveData<SearchBusinessUseCase.SearchUiState> = _searchResult

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun search(location: Location) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val resultState = searchBusinessUseCase.searchPizzaAndBeer(location)
                _searchResult.postValue(resultState)
            }catch (t: Throwable) {
                _searchResult.postValue(SearchBusinessUseCase.SearchUiState.Failure)
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun searchMore(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val resultState = searchBusinessUseCase.searchMore()
                _searchResult.postValue(resultState)
            }catch (t: Throwable) {
                _searchResult.postValue(SearchBusinessUseCase.SearchUiState.Failure)
            }finally {
                _isLoading.value = false
            }
        }
    }

}