package com.alirezahr.products.presentation.detail

import com.alirezahr.products.domain.model.Product

data class DetailProductState(
    val product:Product?=null,
    val error:String?=null
)