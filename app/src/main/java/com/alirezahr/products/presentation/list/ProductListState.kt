package com.alirezahr.products.presentation.list

import com.alirezahr.products.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)