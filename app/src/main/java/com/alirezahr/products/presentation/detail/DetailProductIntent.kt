package com.alirezahr.products.presentation.detail

sealed class DetailProductIntent {
    data class LoadDetailProduct(val productId: Int) : DetailProductIntent()
    object OnBackClick:DetailProductIntent()
    object BookMarkClick : DetailProductIntent()
}