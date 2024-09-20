package com.example.fetchrewardscodingexercise.domain.uc

import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.data.models.ItemDTO
import com.example.fetchrewardscodingexercise.data.remote.FetchRewardsApi
import javax.inject.Inject

class GetItemsUC @Inject constructor(
    private val fetchRewardsApi: FetchRewardsApi
) {
    suspend operator fun invoke(): List<Item> {
        val dtoItems = fetchRewardsApi.getItems()

        val filteredItems = filterAndMap(dtoItems)
        return sortItems(filteredItems)
    }

    private fun filterAndMap(dtoItems: List<ItemDTO>): List<Item> {
        return dtoItems
            .filter { !it.name.isNullOrBlank() }
            .map { dtoItem ->
                Item(
                    id = dtoItem.id,
                    listId = dtoItem.listId,
                    name = dtoItem.name!!
                )
            }
    }

    private fun sortItems(items: List<Item>): List<Item> {
        return items.sortedWith(
            compareBy<Item> { it.listId }
                .thenBy { it.name }
        )
    }
}