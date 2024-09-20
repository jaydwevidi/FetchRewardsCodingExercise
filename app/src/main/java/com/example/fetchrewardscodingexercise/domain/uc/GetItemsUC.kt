package com.example.fetchrewardscodingexercise.domain.uc

import com.example.fetchrewardscodingexercise.data.models.Item
import com.example.fetchrewardscodingexercise.data.remote.FetchRewardsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetItemsUC @Inject constructor(
    private val fetchRewardsApi: FetchRewardsApi // Injecting the FetchRewardsApi using DI
) {

    suspend operator fun invoke() = flow {

        val dtoItems = fetchRewardsApi.getItems()

        val filteredItems = dtoItems.mapNotNull { item ->
            val name = item.name
            if (name is String && name.isNotEmpty()) {
                Item(
                    id = item.id,
                    listId = item.listId,
                    name = name
                )
            } else {
                null
            }
        }

        emit (filteredItems)
    }
}