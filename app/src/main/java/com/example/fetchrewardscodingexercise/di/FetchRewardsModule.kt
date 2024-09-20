package com.example.fetchrewardscodingexercise.di

import com.example.fetchrewardscodingexercise.data.remote.FetchRewardsApi
import com.example.fetchrewardscodingexercise.domain.uc.GetItemsUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  FetchRewardsModule {

    @Provides
    fun provideGetItemsUC(fetchRewardsApi: FetchRewardsApi): GetItemsUC {
        return GetItemsUC(fetchRewardsApi)
    }

    @Provides
    @Singleton
    fun provideFetchRewardsApiService(): FetchRewardsApi {

        val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"


        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: FetchRewardsApi = getRetrofit().create(FetchRewardsApi::class.java)
        return apiService

    }
}