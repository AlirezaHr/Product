package com.alirezahr.products.data.repository

import com.alirezahr.products.data.mapper.toDomain
import com.alirezahr.products.data.remote.api.ProductApiService
import com.alirezahr.products.data.remote.base.BaseDataHandler
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.data.remote.base.RetrofitConnectionHandler
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val productApi: ProductApiService,
    private val retrofitConnection: RetrofitConnectionHandler
) : ProductRepository, BaseDataHandler() {
    override suspend fun getProducts(): Flow<Resource<List<Product>>> =
        performData {
            retrofitConnection.execute { productApi.getProductList() }
                .let { res ->
                    when (res.status) {
                        Resource.Status.SUCCESS -> {
                            val map = res.data?.map { it.toDomain() }
                            Resource.success(map, res.code)
                        }

                        Resource.Status.ERROR -> {
                            Resource.error(res.message ?: "Unknown error")
                        }

                        else -> Resource.loading()
                    }
                }
        }
}
