package com.example.noexpire.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Product-Table")
data class ProductItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo("product-name")
    val name: String?= "",
    @ColumnInfo("product-purchase")
    val purchaseDate: Long,
    @ColumnInfo("product-expiry")
    val expiryDate: Long,
    @ColumnInfo("product-imageId")
    val imageId: String? = "",
    @ColumnInfo("product-quantity")
    val quantity: String? = "1",
    @ColumnInfo("product-remDays")
    val remainingDays: Int = 0
)
