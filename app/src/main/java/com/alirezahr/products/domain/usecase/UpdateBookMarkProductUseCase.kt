package com.alirezahr.products.domain.usecase

import com.alirezahr.products.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateBookMarkProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(isBookMark: Boolean, productId: Int){
        repository.updateBookMark(productId, isBookMark)
    }
}