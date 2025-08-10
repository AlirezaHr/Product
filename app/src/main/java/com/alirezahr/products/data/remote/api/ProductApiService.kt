package com.alirezahr.products.data.remote.api

import com.alirezahr.products.data.remote.dto.ProductDto
import com.alirezahr.products.data.remote.base.Resource
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {

    @GET("/products")
    suspend fun getProductList(): Response<List<ProductDto>>
}