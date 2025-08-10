package com.alirezahr.products.domain.repository

import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Flow<Resource<List<Product>>>
}