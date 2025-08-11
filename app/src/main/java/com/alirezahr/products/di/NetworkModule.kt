package com.alirezahr.products.di

import com.alirezahr.products.data.remote.api.ProductApiService
import com.alirezahr.products.domain.repository.ProductRepository
import com.alirezahr.products.domain.usecase.GetProductListByIdUseCase
import com.alirezahr.products.domain.usecase.GetProductListUseCase
import com.alirezahr.products.domain.usecase.ShowOnlyBookMarkProductUseCase
import com.alirezahr.products.domain.usecase.UpdateBookMarkProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGetProductUseCase(repository: ProductRepository): GetProductListUseCase =
        GetProductListUseCase(repository)

    @Provides
    @Singleton
    fun provideGetByIdProductUseCase(repository: ProductRepository): GetProductListByIdUseCase =
        GetProductListByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateBookMarkProductUseCase(repository: ProductRepository): UpdateBookMarkProductUseCase =
        UpdateBookMarkProductUseCase(repository)

    @Provides
    @Singleton
    fun provideShowOnlyBookmarkUseCase(repository: ProductRepository): ShowOnlyBookMarkProductUseCase =
        ShowOnlyBookMarkProductUseCase(repository)
}