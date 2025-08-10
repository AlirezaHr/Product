package com.alirezahr.products.data.mapper

import com.alirezahr.products.data.local.ProductEntity
import com.alirezahr.products.data.local.Rating
import com.alirezahr.products.data.remote.dto.ProductDto
import com.alirezahr.products.data.remote.dto.RatingDto
import com.alirezahr.products.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(id, title, price, description, category, image, rating)
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id,
        title,
        price,
        description,
        category,
        image,
        rating.toRatingDto(),
        isBookMark
    )
}

fun Rating.toRatingDto(): RatingDto = RatingDto(rate, count)

fun RatingDto.toRating(): Rating = Rating(rate, count)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    image = image,
    rating = rating.toRating(),
    isBookMark = isBookMark,
    category = category
)