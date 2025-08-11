package com.alirezahr.products.presentation.list

import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.data.remote.base.Error

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: Error? = null,
    val searchQuery: String = "",
    val isBookMark: Boolean = false,
    val hasLoaded: Boolean = false
)