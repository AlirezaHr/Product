package com.alirezahr.products.di

import com.alirezahr.products.data.repository.ProductRepositoryImp
import com.alirezahr.products.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object RepositoryModule {
    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule{
        @Binds
        @Singleton
        abstract fun productRepository(
            productRepositoryImp: ProductRepositoryImp
        ):ProductRepository
    }
}