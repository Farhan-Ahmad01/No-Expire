package com.example.noexpire.data

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: ProductDatabase
    lateinit var appContext: Context // Initialize this in NoExpireApp

    val productRepository by lazy {
        ProductRepository(productDao = database.ProductDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, ProductDatabase::class.java, "noexpire.db").build()
    }
}