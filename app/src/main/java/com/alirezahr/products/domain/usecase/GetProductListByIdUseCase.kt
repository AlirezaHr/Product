package com.alirezahr.products.domain.usecase

import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductListByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator  fun invoke(productId: Int): Flow<Product> =
        repository.getProductById(productId)
}