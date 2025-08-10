package com.alirezahr.products.data.mapper

import com.alirezahr.products.data.remote.dto.ProductDto
import com.alirezahr.products.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(id, title, price, description, category, image, rating)
}