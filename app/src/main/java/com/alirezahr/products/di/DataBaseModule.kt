package com.alirezahr.products.di

import android.content.Context
import androidx.room.Room
import com.alirezahr.products.data.local.AppDatabase
import com.alirezahr.products.data.local.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "product_db").build()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
}