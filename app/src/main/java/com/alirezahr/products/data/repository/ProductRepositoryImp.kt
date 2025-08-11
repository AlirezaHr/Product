package com.alirezahr.products.data.repository

import com.alirezahr.products.data.local.ProductDao
import com.alirezahr.products.data.mapper.toDomain
import com.alirezahr.products.data.mapper.toEntity
import com.alirezahr.products.data.remote.api.ProductApiService
import com.alirezahr.products.data.remote.base.BaseHandleRetrofitConnection
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val productApi: ProductApiService,
    private val productDao: ProductDao
) : ProductRepository, BaseHandleRetrofitConnection() {
    override fun getProducts(): Flow<Resource<List<Product>>> =
        flow<Resource<List<Product>>> {
            emit(Resource.loading())
            makeConnection { productApi.getProductList() }.let { response ->
                when (response.status) {
                    Resource.Status.SUCCESS -> {
                        val products = response.data?.map { it.toDomain() } ?: emptyList()

                        with(productDao) {
                            if (getAllProducts().first().isEmpty()) {
                                insertProducts(products.map { it.toEntity() })
                            }
                        }

                        emit(Resource.success(products, response.code))
                    }

                    Resource.Status.ERROR -> {
                        getCacheOrShowError(this, response.message ?: "Unknown error")
                    }

                    else -> emit(Resource.loading())
                }
            }

        }.catch { message ->
            getCacheOrShowError(this, message.message!!)
        }

    private suspend fun getCacheOrShowError(
        flowCollector: FlowCollector<Resource<List<Product>>>,
        message: String
    ) {
        productDao.getAllProducts()
            .map { it.map { entity -> entity.toDomain() } }
            .collect { cached ->
                if (cached.isNotEmpty()) {
                    flowCollector.emit(Resource.success(cached))
                } else {
                    flowCollector.emit(Resource.error(message))
                }
            }
    }

    override fun getProductById(productId: Int): Flow<Product> =
        productDao.getProductById(productId)
            .mapNotNull { it?.toDomain() }

    override suspend fun updateBookMark(productId: Int, isBookMark: Boolean) {
        productDao.updateBookMarkProduct(!isBookMark, productId)
    }

    override fun getOnlyBookMarkProduct(): Flow<List<Product>> =
        productDao.getOnlyBookMarkProduct()
            .map { entities -> entities.map { it.toDomain() } }
}
