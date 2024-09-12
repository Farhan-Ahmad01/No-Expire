package com.example.noexpire.viewModels


import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Logger.LogcatLogger
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noexpire.data.Graph
import com.example.noexpire.data.ProductItem
import com.example.noexpire.data.ProductRepository
import com.example.noexpire.worker.CheckExpiryWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val productRepository: ProductRepository = Graph.productRepository
): ViewModel() {

    private val _pruducts = MutableStateFlow<List<ProductItem>>(emptyList())
    val products: StateFlow<List<ProductItem>> = _pruducts.asStateFlow()


    init {
        Log.d("all", "getAllRan")
        getAllProducts()
        // Schedule a daily check for products close to expiry
        Log.d("expire", "epireRan")
        scheduleDailyExpiryCheck()
    }

    fun getProductsCloseToExpiry(days: Int) {
        viewModelScope.launch {
            productRepository.getProductsCloseToExpiry(days).collect {productList ->
                productList.forEach {product ->
                    product.name?.let { sendNotification(it, product.remainingDays) }

                }

            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .collect() {productList ->
                    _pruducts.value = productList
                }
        }
    }

//    fun addProduct(product: ProductItem) {
//        viewModelScope.launch(Dispatchers.IO) {
//            productRepository.addProduct(product)
//        }
//    }

    fun addProduct(product: ProductItem) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.addProduct(product)

            // Check if the product is expiring soon (less than or equal to 2 days)
            if (product.remainingDays <= 5) {
                // Send a notification immediately
                withContext(Dispatchers.Main) { // Ensure UI-related work is done on the Main thread
                    sendNotification(product.name ?: "Unknown Product", product.remainingDays)
                }
            }

//            if (product.remainingDays <= 0) {
//                // Send a notification immediately
//                withContext(Dispatchers.Main) { // Ensure UI-related work is done on the Main thread
//                    sendNotification(product.name ?: "Unknown Product", product.remainingDays)
//                }
//            }

        }
    }


    fun deleteProduct(product: ProductItem) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteProduct(product)
        }
    }

    fun updateProduct(product: ProductItem) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.updateProduct(product)
        }
    }

    fun getProductById(id: Long) : Flow<ProductItem> {
        return productRepository.getProductById(id)
    }

    private fun sendNotification(productName: String, remainingDays: Int) {
        val notificationManager = Graph.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            NotificationCompat.Builder(Graph.appContext, "expiry_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Product Expiring Soon!")
                .setContentText("$productName will expire in ${remainingDays} days.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        notificationManager.notify(productName.hashCode(), notification)

    }

    private fun scheduledDailyExpiryCheck() {
        val workRequest = PeriodicWorkRequestBuilder<CheckExpiryWorker>(1, TimeUnit.DAYS)
            .build()
        WorkManager.getInstance(Graph.appContext).enqueue(workRequest)
    }

    private fun scheduleDailyExpiryCheck() {
        val workRequest = PeriodicWorkRequestBuilder<CheckExpiryWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(Graph.appContext).enqueue(workRequest)
    }


}