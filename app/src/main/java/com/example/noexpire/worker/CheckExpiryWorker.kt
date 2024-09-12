package com.example.noexpire.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.noexpire.data.Graph
import com.example.noexpire.data.ProductDao
import com.example.noexpire.data.ProductDatabase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class CheckExpiryWorker(
    private val context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val productRepository = Graph.productRepository
        val productsCloseToExpiry = productRepository.getProductsCloseToExpiry(2).first()

        productsCloseToExpiry.forEach {product ->
            product.name?.let { sendNotification(it, product.remainingDays) }

        }
        return Result.success()
    }

    private fun sendNotification(productName: String, remainingDays: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "expiry_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Product Expiring Soon!")
            .setContentText("$productName will expire in $remainingDays days.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(productName.hashCode(), notification)
    }


}