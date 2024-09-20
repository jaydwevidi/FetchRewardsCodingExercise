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
object FetchRewardsModule {

    private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFetchRewardsApiService(retrofit: Retrofit): FetchRewardsApi {
        return retrofit.create(FetchRewardsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetItemsUC(fetchRewardsApi: FetchRewardsApi): GetItemsUC {
        return GetItemsUC(fetchRewardsApi)
    }
}