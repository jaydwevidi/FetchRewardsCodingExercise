package com.example.fetchrewardscodingexercise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.domain.uc.GetItemsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class UserAction {
    data class Filter(val filterText: String) : UserAction()
    data object Refresh : UserAction()
}

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Success(val items: List<Item>, val filterText: String) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
}

interface MainActivityVMInterface {
    val state: StateFlow<MainScreenState>
    fun handleUserAction(userAction: UserAction)
}

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val getItemsUC: GetItemsUC
) : ViewModel(), MainActivityVMInterface {

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    override val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private var allItems: List<Item> = emptyList()

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            try {
                val items = getItemsUC()
                allItems = items
                _state.value = MainScreenState.Success(items, "")
            } catch (e: IOException) {
                _state.value = MainScreenState.Error("Network error occurred.")
            } catch (e: HttpException) {
                _state.value = MainScreenState.Error("Server error occurred.")
            } catch (e: Exception) {
                _state.value = MainScreenState.Error("An unexpected error occurred.")
            }
        }
    }

    override fun handleUserAction(userAction: UserAction) {
        when (userAction) {
            is UserAction.Filter -> applyFilter(userAction.filterText)
            is UserAction.Refresh -> refreshItems()
        }
    }

    private fun applyFilter(filterText: String) {
        val currentState = _state.value
        if (currentState is MainScreenState.Success) {
            val filteredItems = if (filterText.isEmpty()) {
                allItems
            } else {
                allItems.filter { item ->
                    item.name.contains(filterText, ignoreCase = true) ||
                            item.id.toString().contains(filterText) ||
                            item.listId.toString().contains(filterText)
                }
            }
            _state.value = MainScreenState.Success(filteredItems, filterText)
        }
    }

    private fun refreshItems() {
        _state.value = MainScreenState.Loading
        fetchItems()
    }
}