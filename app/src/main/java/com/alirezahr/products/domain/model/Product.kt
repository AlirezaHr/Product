package com.alirezahr.products.domain.model

import com.alirezahr.products.data.remote.dto.RatingDto

data class Product (
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto
)