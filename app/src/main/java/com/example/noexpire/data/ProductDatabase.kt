package com.example.noexpire.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =[ProductItem::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun ProductDao(): ProductDao
}