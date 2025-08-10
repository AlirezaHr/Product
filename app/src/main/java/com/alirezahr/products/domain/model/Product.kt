package com.alirezahr.products.domain.model

import com.alirezahr.products.data.remote.dto.RatingDto

data class Product (
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto,
    var isBookMark: Boolean = false
){
    fun briefTitle(maxLength: Int = 18): String {
        return if (title.length > maxLength) {
            title.take(maxLength).trimEnd() + "..."
        } else {
            title
        }
    }
}