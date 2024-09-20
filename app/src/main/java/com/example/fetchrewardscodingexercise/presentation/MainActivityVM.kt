package com.example.fetchrewardscodingexercise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.data.models.ItemDTO
import com.example.fetchrewardscodingexercise.domain.uc.GetItemsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val getItemsUC: GetItemsUC
): ViewModel() {

    val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)

    val state = _state.asStateFlow()

    init {
        println("MainActivityVM init")
        viewModelScope.launch {
            getItemsUC.invoke().collect{
                _state.value = MainScreenState.Success(it)
            }
        }

    }
}


sealed class MainScreenState{
    data class Success(val data: List<Item>) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
    object Loading : MainScreenState()
}

