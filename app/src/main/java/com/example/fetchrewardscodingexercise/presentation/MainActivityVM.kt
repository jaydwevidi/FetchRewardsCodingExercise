package com.example.fetchrewardscodingexercise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.domain.uc.GetItemsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserAction {
    data class Filter(val filterText: String) : UserAction()
    object Refresh : UserAction()
}

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val getItemsUC: GetItemsUC
) : ViewModel() {

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state = _state.asStateFlow()

    private val _filterText = MutableStateFlow("")
    val filterText = _filterText.asStateFlow()

    private var allItems: List<Item> = emptyList()

    init {
        fetchItems()
        viewModelScope.launch {
            _filterText.collect {
                applyFilter()
            }
        }
    }

    private fun fetchItems() {
        viewModelScope.launch {
            getItemsUC.invoke().collect { items ->
                allItems = items
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val filteredItems = if (_filterText.value.isEmpty()) {
            allItems
        } else {
            allItems.filter {
                it.name.contains(_filterText.value, ignoreCase = true) ||
                        it.id.toString().contains(_filterText.value) ||
                        it.listId.toString().contains(_filterText.value)
            }
        }
        _state.value = MainScreenState.Success(filteredItems)
    }

    fun handleUserAction(userAction: UserAction) {
        when (userAction) {
            is UserAction.Filter -> {
                _filterText.value = userAction.filterText
            }
            is UserAction.Refresh -> {
                _state.value = MainScreenState.Loading
                fetchItems()
                
            }
        }
    }
}

sealed class MainScreenState {
    data class Success(val data: List<Item>) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
    object Loading : MainScreenState()
}