package com.alirezahr.products.domain.usecase

import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke():Flow<Resource<List<Product>>> = repository.getProducts()
}