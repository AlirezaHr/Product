package com.alirezahr.products.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>) :List<Long>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int): Flow<ProductEntity?>

    @Query("UPDATE products SET isBookmark = :isBookmark WHERE id = :productId")
    suspend fun updateBookMarkProduct(isBookmark: Boolean, productId: Int)

    @Query("SELECT * FROM products WHERE isBookMark = TRUE")
    fun getOnlyBookMarkProduct(): Flow<List<ProductEntity>>
}