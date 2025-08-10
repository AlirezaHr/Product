package com.alirezahr.products.data.remote.dto

import kotlinx.serialization.SerialName

data class ProductDto(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("price")
    val price: Float,

    @SerialName("description")
    val description: String,

    @SerialName("category")
    val category: String,

    @SerialName("image")
    val image: String,

    @SerialName("rating")
    val rating: RatingDto
)