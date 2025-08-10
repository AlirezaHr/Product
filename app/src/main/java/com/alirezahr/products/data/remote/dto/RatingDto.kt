package com.alirezahr.products.data.remote.dto

import kotlinx.serialization.SerialName

data class RatingDto(
    @SerialName("rate")
    val rate: Float,

    @SerialName("count")
    val count: Int
)