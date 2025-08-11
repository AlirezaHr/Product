package com.alirezahr.products.domain.usecase

import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowOnlyBookMarkProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = repository.getOnlyBookMarkProduct()
}