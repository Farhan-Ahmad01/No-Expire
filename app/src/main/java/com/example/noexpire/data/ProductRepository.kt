package com.example.noexpire.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    suspend fun addProduct(productItem: ProductItem) {
        productDao.addProduct(productItem)
    }

    suspend fun updateProduct(productItem: ProductItem) {
        productDao.updateProduct(productItem)
    }

    suspend fun deleteProduct(productItem: ProductItem) {
        productDao.deleteProduct(productItem)
    }

    fun getAllProducts(): Flow<List<ProductItem>> = productDao.getAllProducts()

    fun getProductById(id: Long) : Flow<ProductItem> {
        return productDao.getProductById(id)
    }

    fun getProductsCloseToExpiry(days: Int): Flow<List<ProductItem>> = productDao.getProductsCloseToExpiry(days)

}