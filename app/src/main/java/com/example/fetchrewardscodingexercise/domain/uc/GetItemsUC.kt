package com.example.fetchrewardscodingexercise.domain.uc

import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.data.models.ItemDTO
import com.example.fetchrewardscodingexercise.data.remote.FetchRewardsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetItemsUC @Inject constructor(
    private val fetchRewardsApi: FetchRewardsApi
) {

    suspend operator fun invoke(): Flow<List<Item>> = flow {
        val dtoItems = fetchRewardsApi.getItems()

        val filteredItems = filterAndMap(dtoItems)
        val sortedItems = sortItems(filteredItems)

        emit(sortedItems)
    }

    private fun filterAndMap(dtoItems: List<ItemDTO>): List<Item> {
        return dtoItems.mapNotNull { dtoItem ->
            val name = dtoItem.name as? String
            if (!name.isNullOrBlank()) {
                Item(
                    id = dtoItem.id,
                    listId = dtoItem.listId,
                    name = name
                )
            } else {
                null
            }
        }
    }

    private fun sortItems(items: List<Item>): List<Item> {
        return items.sortedWith(
            compareBy<Item> { it.listId }
                .thenBy { it.name }
        )
    }
}