package com.example.fetchrewardscodingexercise.data.remote

import com.example.fetchrewardscodingexercise.data.models.ItemDTO
import retrofit2.http.GET

interface FetchRewardsApi
{

    @GET("/hiring.json")
    suspend fun getItems(): List<ItemDTO>

}