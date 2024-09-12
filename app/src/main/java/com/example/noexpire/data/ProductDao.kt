package com.example.noexpire.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun addProduct(productEntity: ProductItem)

    @Update
    abstract suspend fun updateProduct(productEntity: ProductItem)

    @Delete
    abstract suspend fun deleteProduct(productEntity: ProductItem)

    @Query("Select * from 'Product-Table'")
    abstract fun getAllProducts(): Flow<List<ProductItem>>

    @Query("Select * from `Product-Table` where id = :id ")
    abstract fun getProductById(id: Long): Flow<ProductItem>

    @Query("Select * from `Product-Table` where `product-remDays` <= :days")
    abstract fun getProductsCloseToExpiry(days : Int): Flow<List<ProductItem>>

}