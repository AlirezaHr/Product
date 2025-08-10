package com.alirezahr.products.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "products")
@TypeConverters(RatingConverter::class)
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val category: String,
    val image: String,
    val rating: Rating,
    val isBookMark: Boolean = false
)

data class Rating(val rate: Float, val count: Int)