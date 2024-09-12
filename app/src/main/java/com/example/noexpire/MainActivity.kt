package com.example.noexpire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.noexpire.Navigation.NavigationGraph
import com.example.noexpire.data.Graph
import com.example.noexpire.screens.HomeScreen

import com.example.noexpire.ui.theme.NoExpireTheme
import com.example.noexpire.viewModels.HomeViewModel
import com.example.noexpire.viewModels.HomeViewModelFactory
import com.example.noexpire.worker.createNotificationChannel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT,
//                android.graphics.Color.TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)
        Graph.provide(this)
//        val homeViewModel: HomeViewModel by viewModels()
        val viewModelFactory = HomeViewModelFactory(Graph.productRepository)
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        setContent {
            NoExpireTheme {
                // A surface container using the 'background' color from the theme
                NavigationGraph(homeViewModel)
            }
        }
        createNotificationChannel(this)
    }
}

































